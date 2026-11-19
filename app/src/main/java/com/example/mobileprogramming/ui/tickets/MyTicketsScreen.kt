package com.example.mobileprogramming.ui.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.ui.components.TicketCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun MyTicketsRoute(
    viewModel: MyTicketsViewModel,
    modifier: Modifier = Modifier
) {
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    MyTicketsScreen(
        tickets = tickets,
        onCancelTicket = { },
        modifier = modifier
    )
}

@Composable
fun MyTicketsScreen(
    tickets: List<Ticket>,
    onCancelTicket: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "My Tickets",
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tickets, key = { it.id }) { ticket ->
                TicketCard(
                    ticket = ticket,
                    onCancelClick = { onCancelTicket(ticket.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MyTicketsScreenPreview() {
    MobileprogrammingTheme {
        MyTicketsScreen(
            tickets = listOf(
                Ticket(id = "1", serviceId = "library", serviceName = "Library", number = 17, peopleAhead = 3),
                Ticket(id = "2", serviceId = "canteen", serviceName = "Canteen", number = 5, peopleAhead = 0)
            ),
            onCancelTicket = {}
        )
    }
}