package com.example.mobileprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobileprogramming.navigation.QueueNavigation
import com.example.mobileprogramming.ui.details.QueueTicketViewModel
import com.example.mobileprogramming.ui.explore.ExploreViewModel
import com.example.mobileprogramming.ui.home.HomeViewModel
import com.example.mobileprogramming.ui.profile.ProfileViewModel
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme
import com.example.mobileprogramming.ui.tickets.MyTicketsViewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels {
        AppContainer.serviceViewModelFactory(::HomeViewModel)
    }

    private val exploreViewModel: ExploreViewModel by viewModels {
        AppContainer.serviceViewModelFactory(::ExploreViewModel)
    }

    private val ticketViewModel: QueueTicketViewModel by viewModels {
        QueueTicketViewModel.Factory
    }

    private val myTicketsViewModel: MyTicketsViewModel by viewModels {
        MyTicketsViewModel.Factory
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContainer.init(applicationContext)

        setContent {
            val isDarkTheme by profileViewModel.isDarkTheme.collectAsStateWithLifecycle()
            MobileprogrammingTheme(darkTheme = isDarkTheme) {
                QueueNavigation(
                    homeViewModel = homeViewModel,
                    exploreViewModel = exploreViewModel,
                    ticketViewModel = ticketViewModel,
                    myTicketsViewModel = myTicketsViewModel,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}