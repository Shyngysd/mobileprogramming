package com.example.mobileprogramming.data.model

import com.example.mobileprogramming.data.repository.DemoQueueServices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QueueWaitEstimateTest {
    private val openService = DemoQueueServices.services.first { it.isOpen }

    @Test
    fun estimateMultipliesQueueSizeByAverageServiceTime() {
        val service = openService.copy(peopleInQueue = 3, averageServiceMinutes = 5)

        assertEquals(15, estimateWaitMinutes(service))
        assertEquals(0, estimateWaitMinutes(service.copy(peopleInQueue = 0)))
    }

    @Test
    fun closedServiceHasNoEstimateEvenWhenPeopleAreWaiting() {
        val service = openService.copy(isOpen = false, peopleInQueue = 3)

        assertNull(estimateWaitMinutes(service))
    }

    @Test
    fun invalidOrUnrepresentableEstimatesAreOmitted() {
        assertNull(estimateWaitMinutes(openService.copy(peopleInQueue = -1)))
        assertNull(estimateWaitMinutes(openService.copy(averageServiceMinutes = 0)))
        assertNull(
            estimateWaitMinutes(
                openService.copy(peopleInQueue = Int.MAX_VALUE, averageServiceMinutes = 2)
            )
        )
    }
}
