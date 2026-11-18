package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import kotlinx.coroutines.flow.StateFlow

interface ServiceRepository {
    val services: StateFlow<List<QueueService>>

    suspend fun refresh()

    fun getServiceById(id: String): QueueService?
}
