package dev.saath.dropwizard.kodein.resources

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/blah")
@Produces(MediaType.TEXT_PLAIN) // Add this
class BlahResource {
    @GET
    @Path("/hello")
    fun getHello() = "Hello"
}

@Path("/test")
class TestResource

@Path("/another")
class AnotherResource
