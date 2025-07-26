package dev.saath.dropwizard.kodein.installers

import io.github.classgraph.ClassGraph
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class ResourceInstallerTest {
    @Test
    fun `test scan finds all resources`() {
        val scanResults =
            ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("dev.saath.dropwizard.kodein.resources")
                .scan()
        scanResults.allClasses.forEach { println(it) }
        val classesWithPath = ResourceInstaller(scanResults).search()
        assertEquals(3, classesWithPath.size, "Resources with @Path were not all found")
    }
}
