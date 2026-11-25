package com.example.mobileprogramming.ui.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.components.QueueTopBar
import com.example.mobileprogramming.ui.components.ServiceCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun ExploreScreen(
    uiState: ExploreUiState,
    onSortChange: (ExploreSortMode) -> Unit,
    onBackClick: () -> Unit,
    onServiceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { QueueTopBar(title = "Explore places", onBackClick = onBackClick) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort by:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FilterChip(
                        selected = uiState.sortMode == ExploreSortMode.QUEUE_SIZE,
                        onClick = { onSortChange(ExploreSortMode.QUEUE_SIZE) },
                        label = { Text("Queue size") }
                    )
                    FilterChip(
                        selected = uiState.sortMode == ExploreSortMode.WAIT_TIME,
                        onClick = { onSortChange(ExploreSortMode.WAIT_TIME) },
                        label = { Text("Wait time") }
                    )
                }
            }

            items(uiState.services, key = { it.id }) { service ->
                ServiceCard(
                    service = service,
                    onClick = { onServiceClick(service.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun ExploreScreenPreview() {
    MobileprogrammingTheme {
        ExploreScreen(
            uiState = ExploreUiState(services = sortServicesByQueue(DemoQueueServices.services)),
            onSortChange = {},
            onBackClick = {},
            onServiceClick = {}
        )
    }
}