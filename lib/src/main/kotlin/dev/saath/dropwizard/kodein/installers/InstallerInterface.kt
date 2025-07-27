package dev.saath.dropwizard.kodein.installers

import io.dropwizard.core.setup.Environment
import io.github.classgraph.ScanResult
import org.kodein.di.BindingsMap
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.instanceOrNull

interface InstallerInterface {
    fun install(
        di: DI,
        environment: Environment,
        clazzes: List<Class<*>>,
    ) {
        clazzes.forEach { clazz ->
            val resource = di.direct.instance<Any>(tag = clazz.name)
            println("resource name: ${resource::class.simpleName}")
            environment.jersey().register(resource)
        }
    }
}

interface AutoScanInstallerInterface : InstallerInterface {
    fun search(scanResults: ScanResult): List<Class<*>>
}

/**
 * DIScanInstallerInterface represents installers that look through
 * Kodein's container tree to install dependecies
 */
interface DIScanInstallerInterface : InstallerInterface {
    fun search(bindings: BindingsMap): List<Class<*>>
}
