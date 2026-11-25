package com.example.mobileprogramming.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.data.repository.TicketRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MyTicketsUiState(
    val waiting: List<Ticket> = emptyList(),
    val served: List<Ticket> = emptyList()
) {
    val isEmpty: Boolean
        get() = waiting.isEmpty() && served.isEmpty()
}

class MyTicketsViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    val uiState: StateFlow<MyTicketsUiState> = repository.getAllTicketsStream()
        .map { tickets ->
            MyTicketsUiState(
                waiting = tickets.filter { !it.isServed },
                served = tickets.filter { it.isServed }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MyTicketsUiState()
        )

    fun markServed(ticket: Ticket) {
        viewModelScope.launch {
            repository.updateTicket(ticket.copy(isServed = true))
        }
    }

    fun deleteTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.deleteTicket(ticket)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { MyTicketsViewModel(AppContainer.ticketRepository) }
        }
    }
}