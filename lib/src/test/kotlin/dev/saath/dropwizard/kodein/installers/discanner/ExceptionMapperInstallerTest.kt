package dev.saath.dropwizard.kodein.installers.discanner

import dev.saath.dropwizard.kodein.installers.autoscanner.ResourceInstaller
import dev.saath.dropwizard.kodein.installers.autoscanner.TestConfiguration
import dev.saath.dropwizard.kodein.installers.discanner.ExceptionMapperInstallerTest.Companion.scanResults
import dev.saath.dropwizard.kodein.resources.AnotherResource
import dev.saath.dropwizard.kodein.resources.BlahResource
import dev.saath.dropwizard.kodein.resources.NotFoundExceptionMapper
import dev.saath.dropwizard.kodein.resources.TestResource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Environment
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.github.classgraph.ClassGraph
import jakarta.ws.rs.client.Client
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.Test

@ExtendWith(DropwizardExtensionsSupport::class)
class ExceptionMapperInstallerTest {
    companion object {
        val app: DropwizardAppExtension<TestConfiguration> =
            DropwizardAppExtension(
                ExceptionMapperTestApplication::class.java,
                TestConfiguration(),
            )

        val scanResults =
            ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("dev.saath.dropwizard.kodein.resources")
                .scan()
    }

    @Test
    fun `not found exception mapper was correctly installed with jersey`() {
        val client: Client = app.client()
        val baseUrl = "http://localhost:${app.localPort}"

        val testResponse = client.target("$baseUrl/test/hello").request().get()

        assertEquals(404, testResponse.status, "Response status was not correct after registering the exception mapper")
        assertEquals(
            NotFoundExceptionMapper.MESSAGE,
            testResponse.readEntity(String::class.java),
            "Response message was not correct after registering the exception mapper",
        )
        testResponse.close()
    }
}

class ExceptionMapperTestApplication : Application<TestConfiguration>() {
    override fun run(
        configuration: TestConfiguration,
        environment: Environment,
    ) {
        val di =
            DI {
                bindSingleton<NotFoundExceptionMapper>(tag = NotFoundExceptionMapper::class.java.name) { NotFoundExceptionMapper() }
                bindSingleton<BlahResource>(tag = BlahResource::class.java.name) { BlahResource() }
                bindSingleton<TestResource>(tag = TestResource::class.java.name) { TestResource() }
                bindSingleton<AnotherResource>(tag = AnotherResource::class.java.name) { AnotherResource() }
            }

        val resourceInstaller = ResourceInstaller()
        val resourceClazzes = resourceInstaller.search(scanResults)
        resourceInstaller.install(di, environment, resourceClazzes)

        val exceptionMapperInstaller = ExceptionMapperInstaller()
        val clazzes = exceptionMapperInstaller.search(di.container.tree.bindings)
        exceptionMapperInstaller.install(di, environment, clazzes)
    }
}
