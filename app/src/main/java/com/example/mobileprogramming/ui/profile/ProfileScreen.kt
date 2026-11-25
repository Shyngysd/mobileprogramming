package com.example.mobileprogramming.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileprogramming.data.model.UserProfile
import com.example.mobileprogramming.ui.components.InfoRow
import com.example.mobileprogramming.ui.components.QueueTopBar
import com.example.mobileprogramming.ui.components.StatCard
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val activeTickets by viewModel.activeTicketsCount.collectAsStateWithLifecycle()
    ProfileScreen(
        profile = profile,
        activeTicketsCount = activeTickets,
        onNameChange = viewModel::updateName,
        onBackClick = onBackClick
    )
}

@Composable
fun ProfileScreen(
    profile: UserProfile,
    activeTicketsCount: Int,
    onNameChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { QueueTopBar(title = "Profile", onBackClick = onBackClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.trim().take(1).uppercase().ifEmpty { "?" },
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = profile.name.ifBlank { "Your name" },
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = profile.name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow(icon = Icons.Filled.Phone, text = profile.phone)
                    InfoRow(icon = Icons.Filled.Email, text = profile.email)
                }
            }

            StatCard(
                value = activeTicketsCount.toString(),
                label = "active tickets",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ProfileScreenPreview() {
    MobileprogrammingTheme {
        ProfileScreen(
            profile = UserProfile(
                name = "Guest",
                phone = "+7 700 000 00 00",
                email = "guest@example.com"
            ),
            activeTicketsCount = 2,
            onNameChange = {},
            onBackClick = {}
        )
    }
}