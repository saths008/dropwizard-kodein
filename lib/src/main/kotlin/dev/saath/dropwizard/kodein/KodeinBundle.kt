package dev.saath.dropwizard.kodein

import dev.saath.dropwizard.kodein.installers.autoscanner.AutoScanInstallerInterface
import dev.saath.dropwizard.kodein.installers.autoscanner.ResourceInstaller
import io.dropwizard.core.Configuration
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassGraphException
import io.github.classgraph.ScanResult
import org.kodein.di.DI
import kotlin.jvm.Throws

class KodeinBundle(
    diModules: List<DI.Module>,
) : ConfiguredBundle<Configuration> {
    val di: DI =
        DI {
            diModules.forEach { import(it) }
        }

    override fun initialize(bootstrap: Bootstrap<*>?) {}

    override fun run(
        configuration: Configuration?,
        environment: Environment?,
    ) {
        val scanRes = searchClassPath()
        listOf<AutoScanInstallerInterface>(ResourceInstaller()).forEach { installer ->
            installer.install(di, environment!!, installer.search(scanRes))
        }
    }

    @Throws(ClassGraphException::class)
    fun searchClassPath(): ScanResult = ClassGraph().enableClassInfo().enableAnnotationInfo().scan()
}
