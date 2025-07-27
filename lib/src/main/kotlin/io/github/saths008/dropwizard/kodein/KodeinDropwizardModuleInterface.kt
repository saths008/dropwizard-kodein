package io.github.saths008.dropwizard.kodein

import org.kodein.di.DI

interface KodeinDropwizardModuleInterface {
    fun configure(): DI.Module
}
