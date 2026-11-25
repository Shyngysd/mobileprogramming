package com.example.mobileprogramming.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    waitingTicketsCount: Int,
    onServiceClick: ((String) -> Unit)?,
    onExploreClick: () -> Unit,
    onTicketsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        waitingTicketsCount = waitingTicketsCount,
        onServiceClick = onServiceClick,
        onRefresh = viewModel::retry,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onCategoryChange = viewModel::onCategoryChange,
        onOpenOnlyChange = viewModel::onOpenOnlyChange,
        onResetFilters = viewModel::resetFilters,
        onExploreClick = onExploreClick,
        onTicketsClick = onTicketsClick,
        onProfileClick = onProfileClick,
        modifier = modifier
    )
}