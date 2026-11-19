package com.example.mobileprogramming.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun TicketCard(
    ticket: Ticket,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Ticket #${ticket.number}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = ticket.serviceName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "People ahead: ${ticket.peopleAhead}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TicketCardPreview() {
    MobileprogrammingTheme {
        TicketCard(
            ticket = Ticket(
                id = "1",
                serviceId = "library",
                serviceName = "Library",
                number = 17,
                peopleAhead = 3
            ),
            onCancelClick = {}
        )
    }
}