package io.github.saths008.dropwizard.kodein

import io.dropwizard.core.Configuration
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassGraphException
import io.github.classgraph.ScanResult
import io.github.saths008.dropwizard.kodein.installers.autoscanner.AutoScanInstallerInterface
import io.github.saths008.dropwizard.kodein.installers.discanner.AllJerseyInstaller
import io.github.saths008.dropwizard.kodein.installers.discanner.DIScanInstallerInterface
import org.kodein.di.DI
import kotlin.jvm.Throws

class KodeinBundle(
    val modules: List<KodeinDropwizardModuleInterface>,
    val diScanInstallers: List<DIScanInstallerInterface> = listOf(AllJerseyInstaller()),
    val autoScaninstallers: List<AutoScanInstallerInterface> = emptyList(),
    val packageToSearch: List<String> = emptyList(),
) : ConfiguredBundle<Configuration> {
    override fun initialize(bootstrap: Bootstrap<*>?) {}

    override fun run(
        configuration: Configuration?,
        environment: Environment?,
    ) {
        configureModules(configuration!!)

        // All modules now have Configuration available
        val di = getDI()

        val scanRes by lazy {
            searchClassPath()
        }

        diScanInstallers.forEach {
            val clazzes = it.search(di.container.tree.bindings)
            it.install(di, environment!!, clazzes)
        }

        autoScaninstallers.forEach {
            val clazzes = it.search(scanRes)
            it.install(di, environment!!, clazzes)
        }
    }

    @Throws(ClassGraphException::class)
    fun searchClassPath(): ScanResult =
        ClassGraph()
            .acceptPackages(*packageToSearch.toTypedArray())
            .enableClassInfo()
            .enableAnnotationInfo()
            .scan()

    fun configureModules(configuration: Configuration) {
        modules.forEach {
            when (it) {
                is ConfigurationAwareModuleInterface -> it.setConfiguration(configuration)
            }
        }
    }

    fun getDI(): DI {
        val di =
            DI {
                modules.forEach { import(it.configure()) }
            }
        return di
    }
}
