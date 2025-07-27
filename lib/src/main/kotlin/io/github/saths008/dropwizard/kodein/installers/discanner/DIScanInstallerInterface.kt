package io.github.saths008.dropwizard.kodein.installers.discanner

import io.github.saths008.dropwizard.kodein.installers.InstallerInterface
import org.kodein.di.BindingsMap

/**
 * DIScanInstallerInterface represents installers that look through Kodein's container tree to
 * install dependecies
 */
interface DIScanInstallerInterface : InstallerInterface {
    fun search(bindings: BindingsMap): List<Class<*>>
}
