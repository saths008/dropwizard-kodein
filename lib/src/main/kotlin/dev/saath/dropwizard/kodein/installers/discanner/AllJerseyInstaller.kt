package dev.saath.dropwizard.kodein.installers.discanner

import org.kodein.di.BindingsMap
import org.kodein.di.DI
import org.kodein.type.jvmType

class AllJerseyInstaller : DIScanInstallerInterface {
    override fun search(bindings: BindingsMap): List<Class<*>> =
        bindings
            .mapNotNull { (key, _) -> key.isRelevantJerseyProvider() }
}

fun DI.Key<*, *, *>.isRelevantJerseyProvider(): Class<*>? {
    val rawClass = this.type.jvmType as? Class<*>

    return when (rawClass) {
        null -> return null
        this.isResource() -> rawClass
        this.isFilter() -> rawClass
        this.isExceptionMapper() -> rawClass
        else -> return null
    }
}
