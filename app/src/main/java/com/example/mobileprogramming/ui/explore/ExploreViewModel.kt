
package com.example.mobileprogramming.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ExploreSortMode {
    QUEUE_SIZE,
    WAIT_TIME
}

data class ExploreUiState(
    val services: List<QueueService> = emptyList(),
    val sortMode: ExploreSortMode = ExploreSortMode.QUEUE_SIZE
)

class ExploreViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())

    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.services.collect { services ->
                _uiState.value = _uiState.value.copy(
                    services = sortServices(
                        services,
                        _uiState.value.sortMode
                    )
                )
            }
        }
    }

    fun changeSortMode(mode: ExploreSortMode) {
        _uiState.value = _uiState.value.copy(
            sortMode = mode,
            services = sortServices(repository.services.value, mode)
        )
    }

    private fun sortServices(
        services: List<QueueService>,
        mode: ExploreSortMode
    ): List<QueueService> {
        return when (mode) {
            ExploreSortMode.QUEUE_SIZE -> sortServicesByQueue(services)
            ExploreSortMode.WAIT_TIME -> sortServicesByWaitTime(services)
        }
    }
}
