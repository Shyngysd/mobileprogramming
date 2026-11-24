package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.ServiceCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoQueueServicesTest {
    @Test
    fun demoServicesHaveStableIdsAndValidQueueData() {
        val services = DemoQueueServices.services
        val expectedNamesById = mapOf(
            "city-clinic" to "City Clinic",
            "smile-dental" to "Smile Dental Care",
            "bank-branch" to "Bank Branch",
            "public-service-center" to "Public Service Center",
            "coffee-point" to "Coffee Point",
            "burger-house" to "Burger House",
            "barber-studio" to "Barber Studio",
            "post-office" to "Post Office"
        )

        assertEquals(expectedNamesById.size, services.size)
        assertEquals(expectedNamesById, services.associate { it.id to it.name })
        assertEquals(ServiceCategory.entries.toSet(), services.map { it.category }.toSet())
        assertTrue(services.all { it.description.isNotBlank() && it.location.endsWith("Demo City") })
        assertTrue(services.all { it.peopleInQueue >= 0 && it.averageServiceMinutes > 0 })
        assertTrue(services.map { it.peopleInQueue }.toSet().size > 1)
        assertTrue(services.map { it.averageServiceMinutes }.toSet().size > 1)
        assertTrue(services.any { it.isOpen })
        assertTrue(services.any { !it.isOpen })
    }
}