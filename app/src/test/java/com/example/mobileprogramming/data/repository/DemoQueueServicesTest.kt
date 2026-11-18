package com.example.mobileprogramming.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoQueueServicesTest {
    @Test
    fun demoServicesHaveStableIdsAndValidQueueData() {
        val services = DemoQueueServices.services
        val expectedNamesById = mapOf(
            "student-service-center" to "Student Service Center",
            "it-help-desk" to "IT Help Desk",
            "library" to "Library",
            "canteen" to "Canteen"
        )

        assertEquals(expectedNamesById.size, services.size)
        assertEquals(expectedNamesById, services.associate { it.id to it.name })
        assertEquals(services.size, services.map { it.category }.toSet().size)
        assertTrue(services.all { it.description.isNotBlank() && it.location.startsWith("Demo campus") })
        assertTrue(services.all { it.peopleInQueue >= 0 && it.averageServiceMinutes > 0 })
        assertTrue(services.map { it.peopleInQueue }.toSet().size > 1)
        assertTrue(services.map { it.averageServiceMinutes }.toSet().size > 1)
        assertTrue(services.any { it.isOpen })
        assertTrue(services.any { !it.isOpen })
    }
}
