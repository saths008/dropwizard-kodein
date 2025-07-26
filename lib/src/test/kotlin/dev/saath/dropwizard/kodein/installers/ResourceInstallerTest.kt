package dev.saath.dropwizard.kodein.installers

import dev.saath.dropwizard.kodein.resources.BlahResource
import io.dropwizard.core.Application
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Environment
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import jakarta.ws.rs.client.Client
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import kotlin.test.Test

@ExtendWith(DropwizardExtensionsSupport::class)
class ResourceInstallerTest {
    companion object {
        val app: DropwizardAppExtension<TestConfiguration> =
            DropwizardAppExtension(
                TestApplication::class.java,
                TestConfiguration(),
            )
    }

    val scanResults =
        ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .acceptPackages("dev.saath.dropwizard.kodein.resources")
            .scan()

    @Test
    fun `test scan finds all resources`() {
        scanResults.allClasses.forEach { println(it) }
        val classesWithPath = ResourceInstaller(scanResults).search()
        assertEquals(3, classesWithPath.size, "Resources with @Path were not all found")
    }

    @Test
    fun `resources are correctly installed with jersey`() {
        val client: Client = app.client()
        val baseUrl = "http://localhost:${app.localPort}"

        val testResponse = client.target("$baseUrl/blah/hello").request().get()

        assertEquals(200, testResponse.status)
        assertEquals("Hello", testResponse.readEntity(String::class.java))
    }
}

class TestApplication : Application<TestConfiguration>() {
    override fun run(
        configuration: TestConfiguration,
        environment: Environment,
    ) {
        val di =
            DI {
                bindSingleton<BlahResource>(tag = BlahResource::class.java.name) { BlahResource() }
            }

        // Would actually be done by the KodeinModule, this is just for this test
        val installer = TestInstaller()
        val clazzes = installer.search()
        installer.install(di, environment, clazzes)
    }
}

class TestConfiguration : Configuration()

class TestInstaller : InstallerInterface {
    override val scanResults: ScanResult
        get() = throw NotImplementedError("Not needed for this test")

    override fun search(): List<Class<*>> = listOf(BlahResource::class.java)
}
