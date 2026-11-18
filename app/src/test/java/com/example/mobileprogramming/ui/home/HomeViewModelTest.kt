package com.example.mobileprogramming.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory
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
        viewModel = HomeViewModel(repository, SavedStateHandle())

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
        viewModel = HomeViewModel(repository, SavedStateHandle())
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
        viewModel = HomeViewModel(repository, SavedStateHandle())
        runCurrent()

        assertTrue(refreshJob.isCancelled)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun searchMatchesNamesAndLocationsAndClearingRestoresTheList() = runTest {
        val services = DemoQueueServices.services
        repository.services.value = services
        viewModel = HomeViewModel(repository, SavedStateHandle())

        viewModel.onSearchQueryChange("Help Desk")
        runCurrent()
        assertEquals(listOf("it-help-desk"), viewModel.uiState.value.services.map { it.id })

        viewModel.onSearchQueryChange("  dInInG aReA  ")
        runCurrent()
        assertEquals(listOf("canteen"), viewModel.uiState.value.services.map { it.id })

        viewModel.onSearchQueryChange("unknown service")
        runCurrent()
        assertTrue(viewModel.uiState.value.services.isEmpty())
        assertEquals("unknown service", viewModel.uiState.value.searchQuery)

        viewModel.onSearchQueryChange("")
        runCurrent()
        assertEquals(services, viewModel.uiState.value.services)
        assertEquals(services, repository.services.value)
        assertEquals("", viewModel.uiState.value.searchQuery)

        viewModel.onSearchQueryChange("   ")
        runCurrent()
        assertEquals(services, viewModel.uiState.value.services)
    }

    @Test
    fun savedQueryIsRestoredWithItsFilteredList() = runTest {
        repository.services.value = DemoQueueServices.services
        val savedState = SavedStateHandle()
        viewModel = HomeViewModel(repository, savedState)
        viewModel.onSearchQueryChange("  LIBRARY  ")
        runCurrent()
        viewModel.viewModelScope.cancel()

        val restoredState = SavedStateHandle(savedState.keys().associateWith { savedState.get<String>(it) })
        viewModel = HomeViewModel(repository, restoredState)

        assertEquals("  LIBRARY  ", viewModel.uiState.value.searchQuery)
        assertEquals(listOf("library"), viewModel.uiState.value.services.map { it.id })
    }

    @Test
    fun searchCategoryAndOpenOnlyApplyTogether() = runTest {
        repository.services.value = DemoQueueServices.services
        viewModel = HomeViewModel(repository, SavedStateHandle())

        viewModel.onSearchQueryChange(" demo campus ")
        viewModel.onCategoryChange(ServiceCategory.LIBRARY)
        viewModel.onOpenOnlyChange(true)
        runCurrent()
        assertEquals(listOf("library"), viewModel.uiState.value.services.map { it.id })

        viewModel.onSearchQueryChange("canteen")
        runCurrent()
        assertTrue(viewModel.uiState.value.services.isEmpty())

        viewModel.onCategoryChange(ServiceCategory.DINING)
        runCurrent()
        assertTrue(viewModel.uiState.value.services.isEmpty())
        assertTrue(viewModel.uiState.value.hasUnfilteredServices)

        viewModel.onOpenOnlyChange(false)
        runCurrent()
        assertEquals(listOf("canteen"), viewModel.uiState.value.services.map { it.id })
    }

    @Test
    fun resetClearsAllConditionsAndRestoresTheOriginalList() = runTest {
        val services = DemoQueueServices.services
        repository.services.value = services
        val savedState = SavedStateHandle()
        viewModel = HomeViewModel(repository, savedState)

        viewModel.onSearchQueryChange("canteen")
        viewModel.onCategoryChange(ServiceCategory.DINING)
        viewModel.onOpenOnlyChange(true)
        runCurrent()
        assertTrue(viewModel.uiState.value.hasActiveFilters)
        assertTrue(viewModel.uiState.value.services.isEmpty())

        viewModel.resetFilters()
        runCurrent()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedCategory)
        assertFalse(state.openOnly)
        assertFalse(state.hasActiveFilters)
        assertEquals(services, state.services)
        assertEquals(services, repository.services.value)

        viewModel.viewModelScope.cancel()
        viewModel = HomeViewModel(repository, savedState)
        assertEquals("", viewModel.uiState.value.searchQuery)

        repository.services.value = emptyList()
        viewModel.onCategoryChange(ServiceCategory.LIBRARY)
        runCurrent()
        assertTrue(viewModel.uiState.value.services.isEmpty())
        assertFalse(viewModel.uiState.value.hasUnfilteredServices)
    }

    @Test
    fun firstLoadCanCompleteWithAnEmptySource() = runTest {
        val firstLoad = CompletableDeferred<Unit>()
        repository.onRefresh = { firstLoad.await() }
        viewModel = HomeViewModel(repository, SavedStateHandle())
        runCurrent()

        assertTrue(viewModel.uiState.value.isInitialLoading)
        assertFalse(viewModel.uiState.value.isRefreshing)

        firstLoad.complete(Unit)
        runCurrent()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.hasUnfilteredServices)
        assertTrue(state.services.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun refreshKeepsFilteredServicesOnFailureAndRetryPublishesNewData() = runTest {
        val demoServices = DemoQueueServices.services
        val library = demoServices.first { it.id == "library" }
        val updatedLibrary = library.copy(peopleInQueue = library.peopleInQueue + 1)
        repository.services.value = demoServices
        viewModel = HomeViewModel(repository, SavedStateHandle())
        viewModel.onSearchQueryChange("library")
        runCurrent()

        val refresh = CompletableDeferred<Unit>()
        repository.onRefresh = { refresh.await() }
        viewModel.retry()
        runCurrent()

        assertTrue(viewModel.uiState.value.isRefreshing)
        assertFalse(viewModel.uiState.value.isInitialLoading)
        assertEquals(listOf(library), viewModel.uiState.value.services)

        refresh.completeExceptionally(IllegalStateException("Demo refresh failed"))
        runCurrent()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(HomeLoadError.SERVICES_REFRESH_FAILED, viewModel.uiState.value.error)
        assertEquals(listOf(library), viewModel.uiState.value.services)

        repository.onRefresh = {
            repository.services.value = demoServices.map { service ->
                if (service.id == library.id) updatedLibrary else service
            }
        }
        viewModel.retry()
        runCurrent()

        assertNull(viewModel.uiState.value.error)
        assertEquals("library", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(updatedLibrary), viewModel.uiState.value.services)
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
