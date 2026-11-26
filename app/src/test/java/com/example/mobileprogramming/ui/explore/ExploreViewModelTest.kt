package com.example.mobileprogramming.ui.explore

import androidx.lifecycle.viewModelScope
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.data.repository.ServiceRepository
import com.example.mobileprogramming.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {
    private val serviceRepository = FakeServiceRepository()
    private val preferences = FakePreferencesRepository()
    private lateinit var viewModel: ExploreViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        serviceRepository.services.value = DemoQueueServices.services
    }

    @After
    fun tearDown() {
        if (::viewModel.isInitialized) viewModel.viewModelScope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun savedSortModeIsRestoredOnStart() = runTest {
        preferences.sortMode.value = "WAIT_TIME"
        viewModel = ExploreViewModel(serviceRepository, preferences)
        runCurrent()

        assertEquals(ExploreSortMode.WAIT_TIME, viewModel.uiState.value.sortMode)
        assertEquals(
            sortServicesByWaitTime(DemoQueueServices.services),
            viewModel.uiState.value.services
        )
    }

    @Test
    fun changingSortModeSavesIt() = runTest {
        viewModel = ExploreViewModel(serviceRepository, preferences)
        runCurrent()

        viewModel.changeSortMode(ExploreSortMode.WAIT_TIME)
        runCurrent()

        assertEquals("WAIT_TIME", preferences.sortMode.value)
        assertEquals(ExploreSortMode.WAIT_TIME, viewModel.uiState.value.sortMode)
    }

    private class FakeServiceRepository : ServiceRepository {
        override val services = MutableStateFlow<List<QueueService>>(emptyList())

        override suspend fun refresh() = Unit

        override fun getServiceById(id: String): QueueService? =
            services.value.firstOrNull { it.id == id }
    }

    private class FakePreferencesRepository : UserPreferencesRepository {
        override val isDarkTheme = MutableStateFlow(false)
        override val sortMode = MutableStateFlow("QUEUE_SIZE")
        override val userName = MutableStateFlow("Guest")

        override suspend fun saveDarkTheme(isDarkTheme: Boolean) {
            this.isDarkTheme.value = isDarkTheme
        }

        override suspend fun saveSortMode(sortMode: String) {
            this.sortMode.value = sortMode
        }

        override suspend fun saveUserName(name: String) {
            userName.value = name
        }
    }
}
