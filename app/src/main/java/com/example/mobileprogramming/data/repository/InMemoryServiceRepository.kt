package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import java.util.Collections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryServiceRepository(
    private val loadServices: () -> List<QueueService> = { DemoQueueServices.services }
) : ServiceRepository {
    private val _services = MutableStateFlow(loadSnapshot())
    override val services: StateFlow<List<QueueService>> = _services.asStateFlow()

    override suspend fun refresh() {
        _services.value = loadSnapshot()
    }

    override fun getServiceById(id: String): QueueService? =
        services.value.firstOrNull { it.id == id }

    private fun loadSnapshot(): List<QueueService> =
        Collections.unmodifiableList(ArrayList(loadServices()))
}
