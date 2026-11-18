package com.example.mobileprogramming.ui.home

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory
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
    private val filters = MutableStateFlow(
        Filters(searchQuery = savedStateHandle.get<String>(SEARCH_QUERY_KEY).orEmpty())
    )
    private val refreshState = MutableStateFlow(RefreshState(isLoading = true))
    private var refreshJob: Job? = null

    val uiState: StateFlow<HomeUiState> = combine(
        repository.services,
        refreshState,
        filters,
        ::buildUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = buildUiState(repository.services.value, refreshState.value, filters.value)
    )

    init {
        retry()
    }

    @MainThread
    fun onSearchQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
        filters.value = filters.value.copy(searchQuery = query)
    }

    @MainThread
    fun onCategoryChange(category: ServiceCategory?) {
        filters.value = filters.value.copy(category = category)
    }

    @MainThread
    fun onOpenOnlyChange(openOnly: Boolean) {
        filters.value = filters.value.copy(openOnly = openOnly)
    }

    @MainThread
    fun resetFilters() {
        savedStateHandle[SEARCH_QUERY_KEY] = ""
        filters.value = Filters()
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

    private fun buildUiState(
        services: List<QueueService>,
        refresh: RefreshState,
        filters: Filters
    ): HomeUiState {
        val searchText = filters.searchQuery.trim()
        val matchingServices = services.filter { service ->
            val matchesSearch = searchText.isEmpty() ||
                service.name.contains(searchText, ignoreCase = true) ||
                service.location.contains(searchText, ignoreCase = true)
            val matchesCategory = filters.category == null || service.category == filters.category
            val matchesOpenOnly = !filters.openOnly || service.isOpen

            matchesSearch && matchesCategory && matchesOpenOnly
        }

        return HomeUiState(
            services = matchingServices,
            isLoading = refresh.isLoading,
            error = refresh.error,
            searchQuery = filters.searchQuery,
            selectedCategory = filters.category,
            openOnly = filters.openOnly,
            hasUnfilteredServices = services.isNotEmpty()
        )
    }

    private data class Filters(
        val searchQuery: String = "",
        val category: ServiceCategory? = null,
        val openOnly: Boolean = false
    )

    private data class RefreshState(
        val isLoading: Boolean,
        val error: HomeLoadError? = null
    )

    private companion object {
        const val SEARCH_QUERY_KEY = "searchQuery"
    }
}
