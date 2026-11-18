package com.example.mobileprogramming.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val status = if (service.isOpen) R.string.service_open else R.string.service_closed

    val content: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(service.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = service.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(stringResource(status), style = MaterialTheme.typography.labelLarge)
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = pluralStringResource(
                        R.plurals.service_people_waiting,
                        service.peopleInQueue,
                        service.peopleInQueue
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (waitMinutes != null) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.service_estimated_wait,
                            waitMinutes,
                            waitMinutes
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    val cardModifier = modifier.fillMaxWidth()
    if (onClick == null) {
        OutlinedCard(modifier = cardModifier, content = content)
    } else {
        OutlinedCard(onClick = onClick, modifier = cardModifier, content = content)
    }
}

@Preview(name = "Open (large text)", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun OpenServiceCardPreview() {
    val service = DemoQueueServices.services.first { it.isOpen }.copy(
        name = "Student Service Center for Academic Records and Enrollment",
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
