package com.example.mobileprogramming.ui.home

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.repository.ServiceRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ServiceRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")
    private val refreshState = MutableStateFlow(RefreshState(isLoading = true))
    private var refreshJob: Job? = null

    val uiState: StateFlow<HomeUiState> = combine(
        repository.services,
        refreshState,
        searchQuery
    ) { services, refresh, query ->
        HomeUiState(
            services = filterServices(services, query),
            isLoading = refresh.isLoading,
            error = refresh.error,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(
            services = filterServices(repository.services.value, searchQuery.value),
            searchQuery = searchQuery.value
        )
    )

    init {
        retry()
    }

    @MainThread
    fun onSearchQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

    @MainThread
    fun retry() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            refreshState.value = RefreshState(isLoading = true)
            var error: HomeLoadError? = null
            try {
                repository.refresh()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                error = HomeLoadError.SERVICES_LOAD_FAILED
            } finally {
                refreshState.value = RefreshState(isLoading = false, error = error)
            }
        }
    }

    private fun filterServices(services: List<QueueService>, query: String): List<QueueService> {
        val searchText = query.trim()
        if (searchText.isEmpty()) return services

        return services.filter { service ->
            service.name.contains(searchText, ignoreCase = true) ||
                service.location.contains(searchText, ignoreCase = true)
        }
    }

    private data class RefreshState(
        val isLoading: Boolean,
        val error: HomeLoadError? = null
    )

    private companion object {
        const val SEARCH_QUERY_KEY = "searchQuery"
    }
}
