
package com.example.mobileprogramming.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.QueueTicket

@Composable
fun ServiceDetailsScreen(
    service: QueueService?,
    ticket: QueueTicket?,
    onBackClick: () -> Unit,
    onJoinQueueClick: () -> Unit,
    onViewTicketClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onBackClick) {
            Text("Back")
        }

        if (service == null) {
            Text("Service not found")
        } else {
            Text("Service Details")

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(service.name)
                    Text(service.description)
                    Text("Location: ${service.location}")

                    if (service.isOpen) {
                        Text("Status: Open")
                    } else {
                        Text("Status: Closed")
                    }

                    Text("People in queue: ${service.peopleInQueue}")

                    val waitMinutes =
                        service.peopleInQueue.toLong() *
                                service.averageServiceMinutes.toLong()

                    if (
                        service.isOpen &&
                        service.peopleInQueue >= 0 &&
                        service.averageServiceMinutes > 0
                    ) {
                        Text("Estimated wait: $waitMinutes minutes")
                    } else {
                        Text("Estimated wait: Not available")
                    }
                }
            }

            if (ticket?.serviceId == service.id) {
                Text("You already have a ticket for this service.")

                Button(
                    onClick = onViewTicketClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View My Ticket #${ticket.ticketNumber}")
                }
            } else if (ticket != null) {
                Text("You already have an active ticket for another service.")
            } else {
                Button(
                    onClick = onJoinQueueClick,
                    enabled = service.isOpen,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Join Queue")
                }
            }
        }
    }
}
