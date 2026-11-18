package com.example.mobileprogramming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mobileprogramming.data.repository.ServiceRepository
import org.junit.Assert.assertSame
import org.junit.Test

class AppContainerTest {
    @Test
    fun factoriesPassTheSameRepositoryToViewModels() {
        val firstFactory = AppContainer.serviceViewModelFactory(::TestViewModel)
        val secondFactory = AppContainer.serviceViewModelFactory(::TestViewModel)

        val first = firstFactory.create(TestViewModel::class.java, CreationExtras.Empty)
        val second = secondFactory.create(TestViewModel::class.java, CreationExtras.Empty)

        assertSame(AppContainer.serviceRepository, first.repository)
        assertSame(first.repository, second.repository)
    }

    private class TestViewModel(val repository: ServiceRepository) : ViewModel()
}
