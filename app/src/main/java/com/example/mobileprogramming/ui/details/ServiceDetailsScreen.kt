package com.example.mobileprogramming.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.Ticket
import com.example.mobileprogramming.data.model.estimateWaitMinutes
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.components.CategoryIcon
import com.example.mobileprogramming.ui.components.EmptyState
import com.example.mobileprogramming.ui.components.InfoRow
import com.example.mobileprogramming.ui.components.QueueTopBar
import com.example.mobileprogramming.ui.components.StatCard
import com.example.mobileprogramming.ui.components.StatusBadge
import com.example.mobileprogramming.ui.components.categoryLabel
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun ServiceDetailsScreen(
    service: QueueService?,
    waitingTicket: Ticket?,
    onBackClick: () -> Unit,
    onJoinQueueClick: () -> Unit,
    onViewTicketClick: (Int) -> Unit
) {
    Scaffold(
        topBar = { QueueTopBar(title = service?.name ?: "Place", onBackClick = onBackClick) }
    ) { innerPadding ->
        if (service == null) {
            EmptyState(
                icon = Icons.Filled.Info,
                title = "Place not found",
                message = "Go back and choose another place.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    if (service.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(service.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = service.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryIcon(category = service.category, size = 56.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(service.name, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = stringResource(categoryLabel(service.category)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            StatusBadge(isOpen = service.isOpen)
                        }
                        Text(service.description, style = MaterialTheme.typography.bodyLarge)
                        InfoRow(icon = Icons.Filled.LocationOn, text = service.location)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        value = service.peopleInQueue.toString(),
                        label = "people in queue",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = estimateWaitMinutes(service)?.toString() ?: "—",
                        label = "minutes to wait",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (waitingTicket != null) {
                    Text(
                        text = "You are already in this queue.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = { onViewTicketClick(waitingTicket.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View my ticket #${waitingTicket.number}")
                    }
                } else {
                    Button(
                        onClick = onJoinQueueClick,
                        enabled = service.isOpen,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (service.isOpen) "Join queue" else "Closed now")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun ServiceDetailsScreenPreview() {
    MobileprogrammingTheme {
        ServiceDetailsScreen(
            service = DemoQueueServices.services.first(),
            waitingTicket = null,
            onBackClick = {},
            onJoinQueueClick = {},
            onViewTicketClick = {}
        )
    }
}