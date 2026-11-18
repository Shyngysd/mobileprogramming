package com.example.mobileprogramming.ui.home

import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.data.repository.ServiceRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val repository = FakeServiceRepository()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        if (::viewModel.isInitialized) viewModel.viewModelScope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun loadsOnStartAndObservesRepositoryUpdates() = runTest {
        val demoServices = DemoQueueServices.services
        repository.services.value = demoServices.take(1)
        viewModel = HomeViewModel(repository)

        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(repository.services.value, viewModel.uiState.value.services)

        runCurrent()

        assertEquals(1, repository.refreshCalls)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)

        repository.services.value = demoServices
        runCurrent()

        assertEquals(demoServices, viewModel.uiState.value.services)
    }

    @Test
    fun retryClearsErrorAndDoesNotStartAnotherRefreshWhileLoading() = runTest {
        repository.onRefresh = { error("Demo refresh failed") }
        viewModel = HomeViewModel(repository)
        runCurrent()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(HomeLoadError.SERVICES_LOAD_FAILED, viewModel.uiState.value.error)

        val refreshResult = CompletableDeferred<Unit>()
        repository.onRefresh = { refreshResult.await() }
        viewModel.retry()
        viewModel.retry()
        runCurrent()

        assertEquals(2, repository.refreshCalls)
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)

        refreshResult.complete(Unit)
        runCurrent()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun cancellationEndsLoadingAndCancelsTheJobWithoutAnError() = runTest {
        lateinit var refreshJob: Job
        repository.onRefresh = {
            refreshJob = currentCoroutineContext().job
            throw CancellationException("Refresh cancelled")
        }
        viewModel = HomeViewModel(repository)
        runCurrent()

        assertTrue(refreshJob.isCancelled)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    private class FakeServiceRepository : ServiceRepository {
        override val services = MutableStateFlow<List<QueueService>>(emptyList())
        var onRefresh: suspend () -> Unit = {}
        var refreshCalls = 0
            private set

        override suspend fun refresh() {
            refreshCalls++
            onRefresh()
        }

        override fun getServiceById(id: String): QueueService? =
            services.value.firstOrNull { it.id == id }
    }
}
