package com.example.mobileprogramming.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.R
import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.estimateWaitMinutes
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun ServiceCard(
    service: QueueService,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val waitMinutes = estimateWaitMinutes(service)

    val content: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryIcon(category = service.category)
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(service.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = stringResource(categoryLabel(service.category)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(isOpen = service.isOpen)
            }

            InfoRow(icon = Icons.Filled.LocationOn, text = service.location)

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pluralStringResource(
                        R.plurals.service_people_waiting,
                        service.peopleInQueue,
                        service.peopleInQueue
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                if (waitMinutes != null) {
                    Text(
                        text = "~$waitMinutes min",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    val cardModifier = modifier.fillMaxWidth()
    if (onClick == null) {
        ElevatedCard(modifier = cardModifier, content = content)
    } else {
        ElevatedCard(onClick = onClick, modifier = cardModifier, content = content)
    }
}

@Preview(name = "Open (large text)", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun OpenServiceCardPreview() {
    val service = DemoQueueServices.services.first { it.isOpen }.copy(
        name = "Public Service Center for Documents, Certificates and Licenses",
        peopleInQueue = 1,
        averageServiceMinutes = 1
    )

    MobileprogrammingTheme {
        ServiceCard(service = service, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "Closed", showBackground = true, widthDp = 320)
@Composable
private fun ClosedServiceCardPreview() {
    val service = DemoQueueServices.services.first { !it.isOpen }

    MobileprogrammingTheme {
        ServiceCard(service = service, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}