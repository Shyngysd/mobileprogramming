
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
