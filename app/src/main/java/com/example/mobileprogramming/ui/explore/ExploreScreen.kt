package com.example.mobileprogramming.ui.explore


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExploreScreen(
    uiState: ExploreUiState,
    onSortChange: (ExploreSortMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Explore Services")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    onSortChange(ExploreSortMode.QUEUE_SIZE)
                }
            ) {
                Text("Queue Size")
            }

            Button(
                onClick = {
                    onSortChange(ExploreSortMode.WAIT_TIME)
                }
            ) {
                Text("Wait Time")
            }
        }

        Text(
            text = if (uiState.sortMode == ExploreSortMode.QUEUE_SIZE) {
                "Sorted by number of people"
            } else {
                "Sorted by waiting time"
            }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(uiState.services) { service ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = service.name)
                        Text(text = "Location: ${service.location}")
                        Text(text = "People in queue: ${service.peopleInQueue}")

                        Text(
                            text = if (service.isOpen &&
                                service.peopleInQueue >= 0 &&
                                service.averageServiceMinutes > 0
                            ) {
                                val minutes = service.peopleInQueue.toLong() *
                                        service.averageServiceMinutes
                                "Estimated wait: $minutes min"
                            } else {
                                "Estimated wait: unavailable"
                            }
                        )
                    }
                }
            }
        }
    }
}
