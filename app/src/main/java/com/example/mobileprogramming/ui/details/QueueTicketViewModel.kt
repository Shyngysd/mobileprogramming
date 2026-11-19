
package com.example.mobileprogramming.ui.details

import androidx.lifecycle.ViewModel
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.QueueTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueTicketViewModel : ViewModel() {

    private val _ticket = MutableStateFlow<QueueTicket?>(null)

    val ticket: StateFlow<QueueTicket?> = _ticket.asStateFlow()

    fun joinQueue(service: QueueService) {
        if (!service.isOpen || _ticket.value != null) {
            return
        }

        val ticketNumber = service.peopleInQueue + 1

        _ticket.value = QueueTicket(
            ticketNumber = ticketNumber,
            serviceId = service.id,
            serviceName = service.name
        )
    }
}
