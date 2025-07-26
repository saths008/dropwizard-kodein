package dev.saath.dropwizard.kodein

import dev.saath.dropwizard.kodein.installers.InstallerInterface
import dev.saath.dropwizard.kodein.installers.ResourceInstaller
import io.dropwizard.core.Configuration
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassGraphException
import io.github.classgraph.ScanResult
import org.kodein.di.DI
import kotlin.jvm.Throws

class KodeinBundle : ConfiguredBundle<Configuration> {
    val di: DI = DI {}

    override fun initialize(bootstrap: Bootstrap<*>?) {
    }

    override fun run(
        configuration: Configuration?,
        environment: Environment?,
    ) {
        val scanRes = searchClassPath()
        listOf<InstallerInterface>(ResourceInstaller(scanRes)).forEach { installer ->
            installer.install(di, environment!!, installer.search())
        }
    }

    @Throws(ClassGraphException::class)
    fun searchClassPath(): ScanResult = ClassGraph().enableClassInfo().scan()
}
