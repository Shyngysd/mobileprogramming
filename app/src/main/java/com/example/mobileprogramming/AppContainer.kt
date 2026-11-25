package com.example.mobileprogramming

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.data.local.QueueDatabase
import com.example.mobileprogramming.data.repository.InMemoryServiceRepository
import com.example.mobileprogramming.data.repository.OfflineTicketRepository
import com.example.mobileprogramming.data.repository.ServiceRepository
import com.example.mobileprogramming.data.repository.TicketRepository

object AppContainer {
    val serviceRepository: ServiceRepository = InMemoryServiceRepository()

    lateinit var ticketRepository: TicketRepository
        private set

    fun init(context: Context) {
        if (!::ticketRepository.isInitialized) {
            val database = QueueDatabase.getDatabase(context.applicationContext)
            ticketRepository = OfflineTicketRepository(database.ticketDao())
        }
    }

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