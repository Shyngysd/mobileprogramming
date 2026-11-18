package com.example.mobileprogramming.data.model

internal fun estimateWaitMinutes(service: QueueService): Int? {
    if (!service.isOpen || service.peopleInQueue < 0 || service.averageServiceMinutes <= 0) {
        return null
    }

    val minutes = service.peopleInQueue.toLong() * service.averageServiceMinutes
    return if (minutes <= Int.MAX_VALUE) minutes.toInt() else null
}
