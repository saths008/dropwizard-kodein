package dev.saath.dropwizard.kodein.installers

import io.github.classgraph.ScanResult

/**
 * ResourceInstaller does
 */
const val JAX_RS_PATH_ANNOTATION = "@Path"

class ResourceInstaller(
    override val scanResults: ScanResult,
) : InstallerInterface {
    override fun search(): List<Class<*>> = scanResults.getClassesWithAnnotation(JAX_RS_PATH_ANNOTATION).loadClasses()
}
