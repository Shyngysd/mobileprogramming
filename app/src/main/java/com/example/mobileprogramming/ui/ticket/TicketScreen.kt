package com.example.mobileprogramming.ui.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.components.EmptyState
import com.example.mobileprogramming.ui.components.InfoRow
import com.example.mobileprogramming.ui.components.QueueTopBar
import com.example.mobileprogramming.ui.components.StatCard
import com.example.mobileprogramming.ui.components.formatTicketTime
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun TicketScreen(
    ticket: Ticket?,
    service: QueueService?,
    onBackClick: () -> Unit,
    onServedClick: (Ticket) -> Unit,
    onMyTicketsClick: () -> Unit
) {
    Scaffold(
        topBar = { QueueTopBar(title = "Your ticket", onBackClick = onBackClick) }
    ) { innerPadding ->
        if (ticket == null) {
            EmptyState(
                icon = Icons.Filled.Info,
                title = "Ticket not found",
                message = "It may have been cancelled.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            TicketDetails(
                ticket = ticket,
                service = service,
                onServedClick = onServedClick,
                onMyTicketsClick = onMyTicketsClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun TicketDetails(
    ticket: Ticket,
    service: QueueService?,
    onServedClick: (Ticket) -> Unit,
    onMyTicketsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = if (ticket.isServed) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentColor = if (ticket.isServed) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Your number", style = MaterialTheme.typography.labelLarge)
                Text("#${ticket.number}", style = MaterialTheme.typography.displaySmall)
                Text(ticket.serviceName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (ticket.isServed) "Served" else "Waiting in queue",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (!ticket.isServed) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    value = ticket.peopleAhead.toString(),
                    label = "people ahead",
                    modifier = Modifier.weight(1f)
                )
                val minutes = service?.let { ticket.peopleAhead * it.averageServiceMinutes }
                StatCard(
                    value = minutes?.toString() ?: "—",
                    label = "minutes to wait",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (service != null) {
                InfoRow(icon = Icons.Filled.LocationOn, text = service.location)
            }
            InfoRow(
                icon = Icons.Filled.DateRange,
                text = "Taken: ${formatTicketTime(ticket.createdAt)}"
            )
        }

        if (!ticket.isServed) {
            Button(
                onClick = { onServedClick(ticket) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I was served")
            }
        }
        OutlinedButton(
            onClick = onMyTicketsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("All my tickets")
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun TicketScreenPreview() {
    val service = DemoQueueServices.services.first()
    MobileprogrammingTheme {
        TicketScreen(
            ticket = Ticket(
                id = 1,
                serviceId = service.id,
                serviceName = service.name,
                number = 12,
                peopleAhead = service.peopleInQueue
            ),
            service = service,
            onBackClick = {},
            onServedClick = {},
            onMyTicketsClick = {}
        )
    }
}