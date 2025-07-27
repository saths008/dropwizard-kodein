package dev.saath.dropwizard.kodein.installers.discanner

import jakarta.ws.rs.Path
import org.kodein.di.BindingsMap
import org.kodein.di.DI
import org.kodein.type.jvmType

class ResourceInstaller : DIScanInstallerInterface {
    override fun search(bindings: BindingsMap): List<Class<*>> =
        bindings
            .mapNotNull { (key, _) -> key.isResource() }
}

fun DI.Key<*, *, *>.isResource(): Class<*>? {
    val rawClass = this.type.jvmType as? Class<*>

    if (rawClass != null &&
        rawClass.isAnnotationPresent(Path::class.java)
    ) {
        return rawClass
    }

    return null
}
