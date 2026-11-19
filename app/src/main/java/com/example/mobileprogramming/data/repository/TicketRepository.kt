package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class TicketRepository {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    private var nextNumber = 1

    fun addTicket(service: QueueService): Ticket {
        val ticket = Ticket(
            id = UUID.randomUUID().toString(),
            serviceId = service.id,
            serviceName = service.name,
            number = nextNumber++,
            peopleAhead = service.peopleInQueue
        )
        _tickets.update { current -> current + ticket }
        return ticket
    }

    fun cancelTicket(ticketId: String) {
        _tickets.update { current -> current.filterNot { it.id == ticketId } }
    }
}