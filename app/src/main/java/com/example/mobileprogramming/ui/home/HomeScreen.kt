package com.example.mobileprogramming.ui.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileprogramming.R
import com.example.mobileprogramming.data.model.ServiceCategory
import com.example.mobileprogramming.data.repository.DemoQueueServices
import com.example.mobileprogramming.ui.components.ServiceCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onServiceClick: ((String) -> Unit)?,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (ServiceCategory?) -> Unit,
    onOpenOnlyChange: (Boolean) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val error = uiState.error

    Scaffold(
        modifier = modifier.fillMaxSize().imePadding(),
        topBar = {
            Surface {
                Column {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        actions = {
                            if (!uiState.isLoading && error == null) {
                                TextButton(onClick = onRefresh) {
                                    Text(stringResource(R.string.home_refresh))
                                }
                            }
                        }
                    )
                    ServiceSearchField(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
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

            item {
                ServiceFilters(
                    uiState = uiState,
                    onCategoryChange = onCategoryChange,
                    onOpenOnlyChange = onOpenOnlyChange,
                    onResetFilters = onResetFilters
                )
            }

            if (uiState.isLoading) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val message = if (uiState.isRefreshing) {
                            R.string.home_refreshing
                        } else {
                            R.string.home_loading
                        }
                        Text(stringResource(message))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else if (error != null) {
                item {
                    Column {
                        val message = when (error) {
                            HomeLoadError.SERVICES_LOAD_FAILED -> R.string.home_load_error
                            HomeLoadError.SERVICES_REFRESH_FAILED -> R.string.home_refresh_error
                        }
                        Text(stringResource(message), color = MaterialTheme.colorScheme.error)
                        TextButton(onClick = onRefresh) {
                            Text(stringResource(R.string.home_retry))
                        }
                    }
                }
            }

            if (
                uiState.services.isEmpty() && !uiState.isInitialLoading &&
                (error == null || uiState.hasUnfilteredServices)
            ) {
                item {
                    if (uiState.hasUnfilteredServices) {
                        Column {
                            Text(stringResource(R.string.home_no_search_results))
                            if (uiState.searchQuery.isNotBlank()) {
                                TextButton(onClick = { onSearchQueryChange("") }) {
                                    Text(stringResource(R.string.home_clear_search))
                                }
                            }
                        }
                    } else {
                        Text(stringResource(R.string.home_no_services))
                    }
                }
            }

            items(uiState.services, key = { service -> service.id }) { service ->
                ServiceCard(
                    service = service,
                    onClick = onServiceClick?.let { callback -> { callback(service.id) } }
                )
            }
        }
    }
}

@Composable
private fun ServiceFilters(
    uiState: HomeUiState,
    onCategoryChange: (ServiceCategory?) -> Unit,
    onOpenOnlyChange: (Boolean) -> Unit,
    onResetFilters: () -> Unit
) {
    val categoryScroll = rememberScrollState()

    LaunchedEffect(uiState.selectedCategory) {
        if (uiState.selectedCategory == null) categoryScroll.scrollTo(0)
    }

    Column {
        Row(
            modifier = Modifier.horizontalScroll(categoryScroll).selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.selectedCategory == null,
                onClick = { onCategoryChange(null) },
                label = { Text(stringResource(R.string.home_category_all)) }
            )
            ServiceCategory.entries.forEach { category ->
                val label = when (category) {
                    ServiceCategory.STUDENT_SERVICES -> R.string.home_category_student_services
                    ServiceCategory.IT_SUPPORT -> R.string.home_category_it_support
                    ServiceCategory.LIBRARY -> R.string.home_category_library
                    ServiceCategory.DINING -> R.string.home_category_dining
                }
                FilterChip(
                    selected = uiState.selectedCategory == category,
                    onClick = { onCategoryChange(category) },
                    label = { Text(stringResource(label)) }
                )
            }
        }

        val openOnlyLabel = stringResource(R.string.home_open_only)
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = openOnlyLabel,
                modifier = Modifier.weight(1f).clearAndSetSemantics {}
            )
            Switch(
                checked = uiState.openOnly,
                onCheckedChange = onOpenOnlyChange,
                modifier = Modifier.semantics { contentDescription = openOnlyLabel }
            )
        }

        if (uiState.hasActiveFilters) {
            TextButton(onClick = onResetFilters) {
                Text(stringResource(R.string.home_reset_filters))
            }
        }
    }
}

@Composable
private fun ServiceSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        label = { Text(stringResource(R.string.home_search_services)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.home_clear_search))
                }
            }
        }
    )
}

@Preview(name = "Services", showBackground = true, widthDp = 360, heightDp = 900)
@Composable
private fun HomeScreenPreview() = PreviewHomeScreen(
    HomeUiState(services = DemoQueueServices.services, isLoading = false)
)

@Preview(name = "No matches", showBackground = true, widthDp = 320, heightDp = 600)
@Composable
private fun HomeScreenNoResultsPreview() = PreviewHomeScreen(
    HomeUiState(
        isLoading = false,
        searchQuery = "Canteen",
        selectedCategory = ServiceCategory.DINING,
        openOnly = true,
        hasUnfilteredServices = true
    )
)

@Preview(name = "Loading", showBackground = true, widthDp = 360, heightDp = 540)
@Composable
private fun HomeScreenLoadingPreview() = PreviewHomeScreen(HomeUiState())

@Preview(name = "Empty source", showBackground = true, widthDp = 360, heightDp = 540)
@Composable
private fun HomeScreenEmptyPreview() = PreviewHomeScreen(HomeUiState(isLoading = false))

@Preview(name = "Load error", showBackground = true, widthDp = 360, heightDp = 540)
@Composable
private fun HomeScreenLoadErrorPreview() = PreviewHomeScreen(
    HomeUiState(isLoading = false, error = HomeLoadError.SERVICES_LOAD_FAILED)
)

@Preview(name = "Refreshing", showBackground = true, widthDp = 360, heightDp = 700)
@Composable
private fun HomeScreenRefreshingPreview() = PreviewHomeScreen(
    HomeUiState(services = DemoQueueServices.services.take(1))
)

@Preview(name = "Refresh error", showBackground = true, widthDp = 360, heightDp = 700)
@Composable
private fun HomeScreenRefreshErrorPreview() = PreviewHomeScreen(
    HomeUiState(
        services = DemoQueueServices.services.take(1),
        isLoading = false,
        error = HomeLoadError.SERVICES_REFRESH_FAILED
    )
)

@Composable
private fun PreviewHomeScreen(uiState: HomeUiState) {
    MobileprogrammingTheme {
        HomeScreen(
            uiState = uiState,
            onServiceClick = null,
            onRefresh = {},
            onSearchQueryChange = {},
            onCategoryChange = {},
            onOpenOnlyChange = {},
            onResetFilters = {}
        )
    }
}
