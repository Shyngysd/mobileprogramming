package com.example.mobileprogramming.ui.home

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: ServiceRepository
) : ViewModel() {
    private val refreshState = MutableStateFlow(RefreshState(isLoading = true))
    private var refreshJob: Job? = null

    val uiState: StateFlow<HomeUiState> = combine(repository.services, refreshState) { services, refresh ->
        HomeUiState(services, refresh.isLoading, refresh.error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(services = repository.services.value)
    )

    init {
        retry()
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

    private data class RefreshState(
        val isLoading: Boolean,
        val error: HomeLoadError? = null
    )
}
