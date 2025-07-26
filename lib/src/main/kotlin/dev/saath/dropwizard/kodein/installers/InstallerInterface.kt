package dev.saath.dropwizard.kodein.installers

import io.dropwizard.core.setup.Environment
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

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
                di.direct.instance<Any>(clazz.kotlin)
                environment.jersey().register(clazz)
            } catch (exception: Exception) {
                println("Failed to register: ${clazz.simpleName}: ${exception.message}")
            }
        }
    }
}
