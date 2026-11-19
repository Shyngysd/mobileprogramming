
package com.example.mobileprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.mobileprogramming.navigation.QueueNavigation
import com.example.mobileprogramming.ui.details.QueueTicketViewModel
import com.example.mobileprogramming.ui.explore.ExploreViewModel
import com.example.mobileprogramming.ui.home.HomeViewModel
import com.example.mobileprogramming.ui.theme.MobileprogrammingTheme

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels {
        AppContainer.serviceViewModelFactory(::HomeViewModel)
    }

    private val exploreViewModel: ExploreViewModel by viewModels {
        AppContainer.serviceViewModelFactory(::ExploreViewModel)
    }

    private val ticketViewModel: QueueTicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MobileprogrammingTheme {
                QueueNavigation(
                    homeViewModel = homeViewModel,
                    exploreViewModel = exploreViewModel,
                    ticketViewModel = ticketViewModel
                )
            }
        }
    }
}
