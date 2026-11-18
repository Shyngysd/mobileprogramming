package com.example.mobileprogramming.ui.home

import com.example.mobileprogramming.data.model.QueueService

data class HomeUiState(
    val services: List<QueueService> = emptyList(),
    val isLoading: Boolean = true,
    val error: HomeLoadError? = null,
    val searchQuery: String = ""
)

enum class HomeLoadError {
    SERVICES_LOAD_FAILED
}
