package dev.saath.dropwizard.kodein

import org.kodein.di.DI

interface KodeinDropwizardModuleInterface {
    fun configure(): DI.Module
}
