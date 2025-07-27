package io.github.saths008.dropwizard.kodein.resources

import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class NotFoundExceptionMapper : ExceptionMapper<NotFoundException> {
    override fun toResponse(exception: NotFoundException?): Response? =
        Response
            .status(404)
            .entity(MESSAGE)
            .type(MediaType.TEXT_PLAIN)
            .build()

    companion object {
        private const val serialVersionUID = 1L
        const val MESSAGE = "Not Found Exception!"
    }
}
