package com.example.mobileprogramming.data.remote

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val category: String,
    @SerialName("people_in_queue")
    val peopleInQueue: Int,
    @SerialName("average_service_minutes")
    val averageServiceMinutes: Int,
    @SerialName("is_open")
    val isOpen: Boolean,
    @SerialName("image_url")
    val imageUrl: String = ""
)

fun PlaceDto.toQueueService(): QueueService = QueueService(
    id = id,
    name = name,
    description = description,
    location = location,
    category = ServiceCategory.entries.firstOrNull { it.name == category }
        ?: ServiceCategory.PUBLIC_SERVICES,
    peopleInQueue = peopleInQueue,
    averageServiceMinutes = averageServiceMinutes,
    isOpen = isOpen,
    imageUrl = imageUrl
)