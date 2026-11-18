package com.example.mobileprogramming.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onServiceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onServiceClick = onServiceClick,
        onRetry = viewModel::retry,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        modifier = modifier
    )
}
