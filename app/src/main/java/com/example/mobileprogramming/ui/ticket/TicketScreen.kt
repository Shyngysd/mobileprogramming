
package com.example.mobileprogramming.ui.ticket

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
fun TicketScreen(
    ticket: QueueTicket?,
    service: QueueService?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Your Queue Ticket")

        if (ticket == null) {
            Text("No active ticket")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Your ticket is active")

                    Text("Ticket Number: #${ticket.ticketNumber}")

                    Text("Service: ${ticket.serviceName}")

                    Text("Status: Active")

                    if (service != null) {
                        Text(
                            "People in queue: ${service.peopleInQueue}"
                        )

                        if (
                            service.isOpen &&
                            service.peopleInQueue >= 0 &&
                            service.averageServiceMinutes > 0
                        ) {
                            val waitMinutes =
                                service.peopleInQueue.toLong() *
                                        service.averageServiceMinutes.toLong()

                            Text(
                                "Estimated wait: $waitMinutes minutes"
                            )
                        } else {
                            Text("Estimated wait: Not available")
                        }

                        Text("Location: ${service.location}")
                    } else {
                        Text("Queue information unavailable")
                    }
                }
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
