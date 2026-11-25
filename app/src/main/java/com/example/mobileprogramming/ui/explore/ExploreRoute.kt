package com.example.mobileprogramming.ui.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExploreRoute(
    viewModel: ExploreViewModel,
    onBackClick: () -> Unit,
    onServiceClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExploreScreen(
        uiState = uiState,
        onSortChange = viewModel::changeSortMode,
        onBackClick = onBackClick,
        onServiceClick = onServiceClick
    )
}