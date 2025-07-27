package dev.saath.dropwizard.kodein.installers.autoscanner

import io.github.classgraph.ScanResult

/**
 * ResourceInstaller does
 */
const val JAX_RS_PATH_ANNOTATION = "jakarta.ws.rs.Path"

class ResourceInstaller : AutoScanInstallerInterface {
    override fun search(scanResults: ScanResult): List<Class<*>> =
        scanResults.getClassesWithAnnotation(JAX_RS_PATH_ANNOTATION).loadClasses()
}
