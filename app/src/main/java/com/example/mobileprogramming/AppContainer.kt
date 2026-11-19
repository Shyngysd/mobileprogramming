package com.example.mobileprogramming

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.data.repository.InMemoryServiceRepository
import com.example.mobileprogramming.data.repository.ServiceRepository
import com.example.mobileprogramming.data.repository.TicketRepository

object AppContainer {
    val serviceRepository: ServiceRepository = InMemoryServiceRepository()
    val ticketRepository = TicketRepository()
    inline fun <reified VM : ViewModel> serviceViewModelFactory(
        crossinline create: (ServiceRepository) -> VM
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer { create(serviceRepository) }
    }

    inline fun <reified VM : ViewModel> serviceViewModelFactory(
        crossinline create: (ServiceRepository, SavedStateHandle) -> VM
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer { create(serviceRepository, createSavedStateHandle()) }
    }
}
