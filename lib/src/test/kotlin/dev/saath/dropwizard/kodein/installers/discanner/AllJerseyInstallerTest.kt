package dev.saath.dropwizard.kodein.installers.discanner

import dev.saath.dropwizard.kodein.installers.autoscanner.TestConfiguration
import dev.saath.dropwizard.kodein.resources.NotFoundExceptionMapper
import dev.saath.dropwizard.kodein.resources.PoweredByResponseFilter
import dev.saath.dropwizard.kodein.resources.TestResource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Environment
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import jakarta.ws.rs.client.Client
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.Test

@ExtendWith(DropwizardExtensionsSupport::class)
class AllJerseyInstallerTest {
    companion object {
        val app: DropwizardAppExtension<TestConfiguration> =
            DropwizardAppExtension(
                AllJerseyInstallerTestApplication::class.java,
                TestConfiguration(),
            )
    }

    @Test
    fun `not found exception mapper was correctly installed with jersey`() {
        val client: Client = app.client()
        val baseUrl = "http://localhost:${app.localPort}"

        val testResponse = client.target("$baseUrl/test/hello").request().get()

        assertEquals(404, testResponse.status, "Resources were not correctly registered")
        assertEquals(
            NotFoundExceptionMapper.MESSAGE,
            testResponse.readEntity(String::class.java),
            "Exception mappers were not registered correctly",
        )

        assertEquals(
            listOf(PoweredByResponseFilter.POWERED_BY_HEADER_VAL),
            testResponse.headers[PoweredByResponseFilter.POWERED_BY_HEADER_KEY],
            "Filters were not correctly registered",
        )
        testResponse.close()
    }
}

class AllJerseyInstallerTestApplication : Application<TestConfiguration>() {
    override fun run(
        configuration: TestConfiguration,
        environment: Environment,
    ) {
        val di =
            DI {
                bindSingleton<NotFoundExceptionMapper>(tag = NotFoundExceptionMapper::class.java.name) { NotFoundExceptionMapper() }
                bindSingleton<TestResource>(tag = TestResource::class.java.name) { TestResource() }
                bindSingleton<PoweredByResponseFilter>(tag = PoweredByResponseFilter::class.java.name) { PoweredByResponseFilter() }
            }

        val allJerseyInstaller = AllJerseyInstaller()
        val clazzes = allJerseyInstaller.search(di.container.tree.bindings)
        allJerseyInstaller.install(di, environment, clazzes)
    }
}
