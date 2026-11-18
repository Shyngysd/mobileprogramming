package com.example.mobileprogramming.ui.explore


import com.example.mobileprogramming.data.model.QueueService

fun sortServicesByQueue(
    services: List<QueueService>
): List<QueueService> {
    return services.sortedBy { it.peopleInQueue }
}
