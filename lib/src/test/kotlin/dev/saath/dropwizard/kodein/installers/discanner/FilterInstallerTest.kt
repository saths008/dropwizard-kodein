package dev.saath.dropwizard.kodein.installers.discanner

import dev.saath.dropwizard.kodein.installers.autoscanner.TestConfiguration
import dev.saath.dropwizard.kodein.resources.BlahResource
import dev.saath.dropwizard.kodein.resources.PoweredByResponseFilter
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
class FilterInstallerTest {
    companion object {
        val app: DropwizardAppExtension<TestConfiguration> =
            DropwizardAppExtension(
                FilterInstallerTestApplication::class.java,
                TestConfiguration(),
            )
    }

    @Test
    fun `container filters were correctly installed with jersey`() {
        val client: Client = app.client()
        val baseUrl = "http://localhost:${app.localPort}"

        val testResponse = client.target("$baseUrl/blah/hello").request().get()

        assertEquals(
            listOf(PoweredByResponseFilter.POWERED_BY_HEADER_VAL),
            testResponse.headers[PoweredByResponseFilter.POWERED_BY_HEADER_KEY],
            "PoweredByResponseFilter did not set correct powered by header",
        )

        testResponse.close()
    }
}

class FilterInstallerTestApplication : Application<TestConfiguration>() {
    override fun run(
        configuration: TestConfiguration,
        environment: Environment,
    ) {
        val di =
            DI {
                bindSingleton<BlahResource>(tag = BlahResource::class.java.name) { BlahResource() }
                bindSingleton<PoweredByResponseFilter>(tag = PoweredByResponseFilter::class.java.name) { PoweredByResponseFilter() }
            }

        val resourceInstaller = ResourceInstaller()
        val resourceClazzes = resourceInstaller.search(di.container.tree.bindings)
        resourceInstaller.install(di, environment, resourceClazzes)

        val filterInstaller = FilterInstaller()
        val filterClazzes = filterInstaller.search(di.container.tree.bindings)
        filterInstaller.install(di, environment, filterClazzes)
    }
}
