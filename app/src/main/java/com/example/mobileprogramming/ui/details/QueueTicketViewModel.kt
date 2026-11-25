package com.example.mobileprogramming.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.data.repository.TicketRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QueueTicketViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    val tickets: StateFlow<List<Ticket>> = repository.getAllTicketsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    private var isJoining = false

    fun waitingTicketFor(serviceId: String): Ticket? =
        tickets.value.firstOrNull { it.serviceId == serviceId && !it.isServed }

    fun joinQueue(service: QueueService, onJoined: (ticketId: Int) -> Unit) {
        if (!service.isOpen || isJoining) return

        val existing = waitingTicketFor(service.id)
        if (existing != null) {
            onJoined(existing.id)
            return
        }

        isJoining = true
        viewModelScope.launch {
            try {
                val ticket = Ticket(
                    serviceId = service.id,
                    serviceName = service.name,
                    number = repository.getNextNumber(),
                    peopleAhead = service.peopleInQueue
                )
                val newId = repository.insertTicket(ticket)
                onJoined(newId)
            } finally {
                isJoining = false
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { QueueTicketViewModel(AppContainer.ticketRepository) }
        }
    }
}