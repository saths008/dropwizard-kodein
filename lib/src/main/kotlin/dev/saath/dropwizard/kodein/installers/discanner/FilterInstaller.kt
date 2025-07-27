package dev.saath.dropwizard.kodein.installers.discanner

import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseFilter
import org.kodein.di.BindingsMap
import org.kodein.di.DI
import org.kodein.type.jvmType

class FilterInstaller : DIScanInstallerInterface {
    override fun search(bindings: BindingsMap): List<Class<*>> =
        bindings
            .mapNotNull { (key, _) -> key.isFilter() }
}

fun DI.Key<*, *, *>.isFilter(): Class<*>? {
    val rawClass = this.type.jvmType as? Class<*>

    if (rawClass != null &&
        (
            ContainerResponseFilter::class.java.isAssignableFrom(rawClass) ||
                ContainerRequestFilter::class.java.isAssignableFrom(rawClass)
        )
    ) {
        return rawClass
    }

    return null
}
