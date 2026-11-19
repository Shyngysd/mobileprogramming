
package com.example.mobileprogramming.ui.explore

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.estimateWaitMinutes

fun sortServicesByQueue(
    services: List<QueueService>
): List<QueueService> {
    return services.sortedBy { it.peopleInQueue }
}

fun sortServicesByWaitTime(
    services: List<QueueService>
): List<QueueService> {
    return services.sortedWith(
        compareBy(nullsLast()) { estimateWaitMinutes(it) }
    )
}
