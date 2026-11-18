package com.example.mobileprogramming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.data.repository.InMemoryServiceRepository
import com.example.mobileprogramming.data.repository.ServiceRepository

object AppContainer {
    val serviceRepository: ServiceRepository = InMemoryServiceRepository()

    inline fun <reified VM : ViewModel> serviceViewModelFactory(
        crossinline create: (ServiceRepository) -> VM
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer { create(serviceRepository) }
    }
}
