package dev.saath.dropwizard.kodein.installers

import io.dropwizard.core.setup.Environment
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

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
