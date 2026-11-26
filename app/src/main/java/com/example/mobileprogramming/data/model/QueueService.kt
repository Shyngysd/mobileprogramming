package com.example.mobileprogramming.data.model

data class QueueService(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val category: ServiceCategory,
    val peopleInQueue: Int,
    val averageServiceMinutes: Int,
    val isOpen: Boolean,
    val imageUrl: String = ""
)