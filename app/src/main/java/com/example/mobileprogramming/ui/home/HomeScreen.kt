package com.example.mobileprogramming.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.R
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.components.ServiceCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onServiceClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val error = uiState.error

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().consumeWindowInsets(innerPadding),
            contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(layoutDirection) + 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                end = innerPadding.calculateEndPadding(layoutDirection) + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Text(
                        text = stringResource(R.string.home_demo_data),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            if (uiState.isLoading) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            } else if (error != null) {
                item {
                    Column {
                        val message = when (error) {
                            HomeLoadError.SERVICES_LOAD_FAILED -> R.string.home_load_error
                        }
                        Text(stringResource(message), color = MaterialTheme.colorScheme.error)
                        TextButton(onClick = onRetry) {
                            Text(stringResource(R.string.home_retry))
                        }
                    }
                }
            } else if (uiState.services.isEmpty()) {
                item { Text(stringResource(R.string.home_no_services)) }
            }

            items(uiState.services, key = { service -> service.id }) { service ->
                ServiceCard(service = service, onClick = { onServiceClick(service.id) })
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 840)
@Composable
private fun HomeScreenPreview() {
    MobileprogrammingTheme {
        HomeScreen(
            uiState = HomeUiState(services = DemoQueueServices.services, isLoading = false),
            onServiceClick = {},
            onRetry = {}
        )
    }
}
