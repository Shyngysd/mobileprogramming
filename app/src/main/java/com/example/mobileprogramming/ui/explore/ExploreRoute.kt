
package com.example.mobileprogramming.ui.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExploreRoute(
    viewModel: ExploreViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExploreScreen(
        uiState = uiState,
        onSortChange = viewModel::changeSortMode
    )
}
