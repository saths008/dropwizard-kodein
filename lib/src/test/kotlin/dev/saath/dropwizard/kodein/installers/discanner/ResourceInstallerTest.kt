package dev.saath.dropwizard.kodein.installers.discanner

import dev.saath.dropwizard.kodein.installers.autoscanner.TestConfiguration
import dev.saath.dropwizard.kodein.installers.discanner.ResourceInstaller
import dev.saath.dropwizard.kodein.resources.BlahResource
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
class ResourceInstallerTest {
    companion object {
        val app: DropwizardAppExtension<TestConfiguration> =
            DropwizardAppExtension(
                ResourceInstallerTestApplication::class.java,
                TestConfiguration(),
            )
    }

    @Test
    fun `not found exception mapper was correctly installed with jersey`() {
        val client: Client = app.client()
        val baseUrl = "http://localhost:${app.localPort}"

        val testResponse = client.target("$baseUrl/blah/hello").request().get()

        assertEquals(200, testResponse.status, "BlahResource was not correctly registered")
        assertEquals(
            "Hello",
            testResponse.readEntity(String::class.java),
            "Response message was incorrect after calling BlahResource's endpoint",
        )
        testResponse.close()
    }
}

class ResourceInstallerTestApplication : Application<TestConfiguration>() {
    override fun run(
        configuration: TestConfiguration,
        environment: Environment,
    ) {
        val di =
            DI {
                bindSingleton<BlahResource>(tag = BlahResource::class.java.name) { BlahResource() }
            }

        val resourceInstaller = ResourceInstaller()
        val resourceClazzes = resourceInstaller.search(di.container.tree.bindings)
        resourceInstaller.install(di, environment, resourceClazzes)
    }
}
