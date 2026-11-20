package com.example.mobileprogramming.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.data.repository.TicketRepository
import kotlinx.coroutines.flow.StateFlow

class MyTicketsViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    val tickets: StateFlow<List<Ticket>> = repository.tickets

    fun cancelTicket(ticketId: String) {
        repository.cancelTicket(ticketId)
    }
    companion object {
        val Factory = viewModelFactory {
            initializer { MyTicketsViewModel(AppContainer.ticketRepository) }
        }
    }
}