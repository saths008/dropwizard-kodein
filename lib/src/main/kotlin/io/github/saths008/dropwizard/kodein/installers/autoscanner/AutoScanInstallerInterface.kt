package io.github.saths008.dropwizard.kodein.installers.autoscanner

import io.github.classgraph.ScanResult
import io.github.saths008.dropwizard.kodein.installers.InstallerInterface

interface AutoScanInstallerInterface : InstallerInterface {
    fun search(scanResults: ScanResult): List<Class<*>>
}
