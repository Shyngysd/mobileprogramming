package com.example.mobileprogramming.ui.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.ui.components.EmptyState
import com.example.mobileprogramming.ui.components.QueueTopBar
import com.example.mobileprogramming.ui.components.TicketCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun MyTicketsRoute(
    viewModel: MyTicketsViewModel,
    onBackClick: () -> Unit,
    onTicketClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyTicketsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onTicketClick = onTicketClick,
        onServedClick = viewModel::markServed,
        onDeleteClick = viewModel::deleteTicket
    )
}

@Composable
fun MyTicketsScreen(
    uiState: MyTicketsUiState,
    onBackClick: () -> Unit,
    onTicketClick: (Int) -> Unit,
    onServedClick: (Ticket) -> Unit,
    onDeleteClick: (Ticket) -> Unit
) {
    Scaffold(
        topBar = { QueueTopBar(title = "My tickets", onBackClick = onBackClick) }
    ) { innerPadding ->
        if (uiState.isEmpty) {
            EmptyState(
                icon = Icons.AutoMirrored.Filled.List,
                title = "No tickets yet",
                message = "Open a place and tap \"Join queue\" to get a ticket.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.waiting.isNotEmpty()) {
                    item { SectionTitle("Waiting (${uiState.waiting.size})") }
                    items(uiState.waiting, key = { it.id }) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onClick = { onTicketClick(ticket.id) },
                            onServedClick = { onServedClick(ticket) },
                            onDeleteClick = { onDeleteClick(ticket) }
                        )
                    }
                }
                if (uiState.served.isNotEmpty()) {
                    item { SectionTitle("History") }
                    items(uiState.served, key = { it.id }) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onClick = { onTicketClick(ticket.id) },
                            onServedClick = {},
                            onDeleteClick = { onDeleteClick(ticket) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MyTicketsScreenPreview() {
    MobileprogrammingTheme {
        MyTicketsScreen(
            uiState = MyTicketsUiState(
                waiting = listOf(
                    Ticket(id = 1, serviceId = "city-clinic", serviceName = "City Clinic", number = 17, peopleAhead = 3)
                ),
                served = listOf(
                    Ticket(id = 2, serviceId = "coffee-point", serviceName = "Coffee Point", number = 5, peopleAhead = 0, isServed = true)
                )
            ),
            onBackClick = {},
            onTicketClick = {},
            onServedClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MyTicketsScreenEmptyPreview() {
    MobileprogrammingTheme {
        MyTicketsScreen(
            uiState = MyTicketsUiState(),
            onBackClick = {},
            onTicketClick = {},
            onServedClick = {},
            onDeleteClick = {}
        )
    }
}