package dev.saath.dropwizard.kodein

import io.dropwizard.core.Configuration

interface ConfigurationAwareModuleInterface {
    /**
     * Method will be called just before injector initialization.
     *
     * @param configuration configuration object
     */
    fun setConfiguration(configuration: Configuration)
}
