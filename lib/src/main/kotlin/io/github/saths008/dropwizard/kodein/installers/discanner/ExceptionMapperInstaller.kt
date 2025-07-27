package io.github.saths008.dropwizard.kodein.installers.discanner

import jakarta.ws.rs.ext.ExceptionMapper
import org.kodein.di.BindingsMap
import org.kodein.di.DI
import org.kodein.type.jvmType

class ExceptionMapperInstaller : DIScanInstallerInterface {
    override fun search(bindings: BindingsMap): List<Class<*>> = bindings.mapNotNull { (key, _) -> key.isExceptionMapper() }
}

fun DI.Key<*, *, *>.isExceptionMapper(): Class<*>? {
    val rawClass = this.type.jvmType as? Class<*>

    if (rawClass == null || !ExceptionMapper::class.java.isAssignableFrom(rawClass)) {
        return null
    }

    return rawClass
}
