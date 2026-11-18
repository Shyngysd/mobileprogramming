package com.example.mobileprogramming.ui.home

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory

data class HomeUiState(
    val services: List<QueueService> = emptyList(),
    val isLoading: Boolean = true,
    val error: HomeLoadError? = null,
    val searchQuery: String = "",
    val selectedCategory: ServiceCategory? = null,
    val openOnly: Boolean = false,
    val hasUnfilteredServices: Boolean = services.isNotEmpty()
) {
    val hasActiveFilters: Boolean
        get() = searchQuery.isNotBlank() || selectedCategory != null || openOnly
}

enum class HomeLoadError {
    SERVICES_LOAD_FAILED
}
