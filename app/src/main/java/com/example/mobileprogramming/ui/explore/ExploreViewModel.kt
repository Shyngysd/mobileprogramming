package com.example.mobileprogramming.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.repository.ServiceRepository
import com.example.mobileprogramming.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    private val repository: ServiceRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedMode = preferencesRepository.sortMode.first()
            val mode = ExploreSortMode.entries.firstOrNull { it.name == savedMode }
                ?: ExploreSortMode.QUEUE_SIZE
            _uiState.value = _uiState.value.copy(
                sortMode = mode,
                services = sortServices(repository.services.value, mode)
            )
        }
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
        viewModelScope.launch {
            preferencesRepository.saveSortMode(mode.name)
        }
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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ExploreViewModel(
                    AppContainer.serviceRepository,
                    AppContainer.userPreferencesRepository
                )
            }
        }
    }
}
