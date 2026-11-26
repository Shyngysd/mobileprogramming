package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.remote.PlacesApiService
import com.example.mobileprogramming.data.remote.nearbyPlacesQuery
import com.example.mobileprogramming.data.remote.toQueueService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkServiceRepository(
    private val apiService: PlacesApiService
) : ServiceRepository {

    private val _services = MutableStateFlow<List<QueueService>>(emptyList())
    override val services: StateFlow<List<QueueService>> = _services.asStateFlow()

    override suspend fun refresh() {
        val response = apiService.getPlaces(nearbyPlacesQuery())
        _services.value = response.elements
            .mapNotNull { it.toQueueService() }
            .distinctBy { it.id }
    }

    override fun getServiceById(id: String): QueueService? =
        services.value.firstOrNull { it.id == id }
}