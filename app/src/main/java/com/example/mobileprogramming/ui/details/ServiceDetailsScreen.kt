
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

@Composable
fun ServiceDetailsScreen(
    service: QueueService?,
    onBackClick: () -> Unit,
    onJoinQueueClick: () -> Unit
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

        Text("Service Details")

        if (service == null) {
            Text("Service not found")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(service.name)
                    Text(service.description)
                    Text("Location: ${service.location}")

                    Text(
                        if (service.isOpen) "Status: Open"
                        else "Status: Closed"
                    )

                    Text("People in queue: ${service.peopleInQueue}")

                    val waitTime =
                        service.peopleInQueue.toLong() *
                                service.averageServiceMinutes

                    Text(
                        if (service.isOpen &&
                            service.peopleInQueue >= 0 &&
                            service.averageServiceMinutes > 0
                        ) {
                            "Estimated wait: $waitTime minutes"
                        } else {
                            "Estimated wait: unavailable"
                        }
                    )
                }
            }

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
