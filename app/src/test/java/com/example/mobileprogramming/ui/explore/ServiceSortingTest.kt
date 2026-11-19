
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
                "1", "Cafe", "Coffee", "SDU",
                ServiceCategory.DINING, 10, 3, true
            ),
            QueueService(
                "2", "Gym", "Fitness", "Almaty",
                ServiceCategory.LIBRARY, 2, 5, true
            )
        )

        val sorted = sortServicesByQueue(services)

        assertEquals("Gym", sorted[0].name)
        assertEquals("Cafe", sorted[1].name)
    }
}

@Test
fun sortServicesByWaitTime_ordersShortestFirst() {
    val services = listOf(
        QueueService(
            "1", "SDU Canteen", "Food", "SDU",
            ServiceCategory.DINING, 10, 3, true
        ),
        QueueService(
            "2", "SDU Library", "Books", "SDU",
            ServiceCategory.LIBRARY, 2, 5, true
        ),
        QueueService(
            "3", "IT Support", "Help", "SDU",
            ServiceCategory.IT_SUPPORT, 5, 4, false
        )
    )

    val sorted = sortServicesByWaitTime(services)

    assertEquals("SDU Library", sorted[0].name)
    assertEquals("SDU Canteen", sorted[1].name)
    assertEquals("IT Support", sorted[2].name)
}

