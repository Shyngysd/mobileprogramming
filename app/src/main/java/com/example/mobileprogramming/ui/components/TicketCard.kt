package com.example.mobileprogramming.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    onServedClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = if (ticket.isServed) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${ticket.number}",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (ticket.isServed) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(text = ticket.serviceName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (ticket.isServed) "Served" else "People ahead: ${ticket.peopleAhead}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (ticket.isServed) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                    Text(
                        text = formatTicketTime(ticket.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!ticket.isServed) {
                    TextButton(onClick = onServedClick) {
                        Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Served")
                    }
                }
                TextButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (ticket.isServed) "Delete" else "Cancel")
                }
            }
        }
    }
}

fun formatTicketTime(millis: Long): String =
    SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()).format(Date(millis))

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TicketCardPreview() {
    MobileprogrammingTheme {
        TicketCard(
            ticket = Ticket(
                id = 1,
                serviceId = "city-clinic",
                serviceName = "City Clinic",
                number = 17,
                peopleAhead = 3
            ),
            onClick = {},
            onServedClick = {},
            onDeleteClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}