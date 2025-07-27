package io.github.saths008.dropwizard.kodein.resources

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import java.io.IOException

class PoweredByResponseFilter : ContainerResponseFilter {
    companion object {
        const val POWERED_BY_HEADER_KEY = "X-Powered-By"
        const val POWERED_BY_HEADER_VAL = "Jersey!"
    }

    @Throws(IOException::class)
    override fun filter(
        requestContext: ContainerRequestContext?,
        responseContext: ContainerResponseContext,
    ) {
        responseContext.getHeaders().add(POWERED_BY_HEADER_KEY, POWERED_BY_HEADER_VAL)
    }
}
