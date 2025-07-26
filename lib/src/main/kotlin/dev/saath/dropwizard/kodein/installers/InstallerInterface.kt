package dev.saath.dropwizard.kodein.installers

import io.dropwizard.core.setup.Environment
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instanceOrNull

interface InstallerInterface {
    val scanResults: io.github.classgraph.ScanResult

    fun search(): List<Class<*>>

    fun install(
        di: DI,
        environment: Environment,
        clazzes: List<Class<*>>,
    ) {
        clazzes.forEach { clazz ->
            try {
                val resource = di.direct.instanceOrNull<Any>(tag = clazz.name)
                environment.jersey().register(resource!!)
            } catch (exception: Exception) {
                println(
                    "==========================================KodeinBundle==========================================",
                )
                println("Failed to register: ${clazz.simpleName}: ${exception.message}")
            }
        }
    }
}
