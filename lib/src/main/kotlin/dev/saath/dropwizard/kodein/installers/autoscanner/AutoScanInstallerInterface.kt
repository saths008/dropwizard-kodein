package dev.saath.dropwizard.kodein.installers.autoscanner

import dev.saath.dropwizard.kodein.installers.InstallerInterface
import io.github.classgraph.ScanResult

interface AutoScanInstallerInterface : InstallerInterface {
    fun search(scanResults: ScanResult): List<Class<*>>
}
