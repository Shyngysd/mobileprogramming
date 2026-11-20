package com.example.mobileprogramming.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileprogramming.data.model.UserProfile
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val activeTickets by viewModel.activeTicketsCount.collectAsStateWithLifecycle()
    ProfileScreen(
        profile = profile,
        activeTicketsCount = activeTickets,
        onNameChange = viewModel::updateName,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    profile: UserProfile,
    activeTicketsCount: Int,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = profile.name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        ProfileInfoRow(label = "Student ID", value = profile.studentId)
        ProfileInfoRow(label = "Email", value = profile.email)
        ProfileInfoRow(label = "Active tickets", value = activeTicketsCount.toString())
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
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
                name = "Student",
                studentId = "240000000",
                email = "student@sdu.edu.kz"
            ),
            activeTicketsCount = 2,
            onNameChange = {}
        )
    }
}