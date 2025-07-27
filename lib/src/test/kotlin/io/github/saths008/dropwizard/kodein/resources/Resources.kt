package io.github.saths008.dropwizard.kodein.resources

import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/blah")
@Produces(MediaType.TEXT_PLAIN)
class BlahResource {
    @GET
    @Path("/hello")
    fun getHello() = "Hello"
}

@Path("/test")
@Produces(MediaType.TEXT_PLAIN)
class TestResource {
    @GET
    @Path("/hello")
    fun getHello(): Nothing = throw NotFoundException("random message")
}

@Path("/another")
class AnotherResource
