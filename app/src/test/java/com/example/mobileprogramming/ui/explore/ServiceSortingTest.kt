package com.example.mobileprogramming.ui.explore

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceSortingTest {

    @Test
    fun sortServicesByQueue_ordersFromSmallestToLargest() {
        val services = listOf(
            QueueService(
                "1", "Cafe", "Coffee", "Demo City",
                ServiceCategory.FOOD, 10, 3, true
            ),
            QueueService(
                "2", "Barber", "Haircuts", "Demo City",
                ServiceCategory.BEAUTY, 2, 5, true
            )
        )

        val sorted = sortServicesByQueue(services)

        assertEquals("Barber", sorted[0].name)
        assertEquals("Cafe", sorted[1].name)
    }

    @Test
    fun sortServicesByWaitTime_ordersShortestFirst() {
        val services = listOf(
            QueueService(
                "1", "Burger House", "Food", "Demo City",
                ServiceCategory.FOOD, 10, 3, true
            ),
            QueueService(
                "2", "Bank Branch", "Cards", "Demo City",
                ServiceCategory.BANKING, 2, 5, true
            ),
            QueueService(
                "3", "Post Office", "Parcels", "Demo City",
                ServiceCategory.POST, 5, 4, false
            )
        )

        val sorted = sortServicesByWaitTime(services)

        assertEquals("Bank Branch", sorted[0].name)
        assertEquals("Burger House", sorted[1].name)
        assertEquals("Post Office", sorted[2].name)
    }
}