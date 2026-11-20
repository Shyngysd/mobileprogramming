package com.example.mobileprogramming.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.ui.details.JoinQueueResult
import com.example.mobileprogramming.ui.details.QueueTicketViewModel
import com.example.mobileprogramming.ui.details.ServiceDetailsScreen
import com.example.mobileprogramming.ui.explore.ExploreRoute
import com.example.mobileprogramming.ui.explore.ExploreViewModel
import com.example.mobileprogramming.ui.home.HomeRoute
import com.example.mobileprogramming.ui.home.HomeViewModel
import com.example.mobileprogramming.ui.profile.ProfileRoute
import com.example.mobileprogramming.ui.profile.ProfileViewModel
import com.example.mobileprogramming.ui.ticket.TicketScreen
import com.example.mobileprogramming.ui.tickets.MyTicketsRoute
import com.example.mobileprogramming.ui.tickets.MyTicketsViewModel

@Composable
fun QueueNavigation(
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel,
    ticketViewModel: QueueTicketViewModel,
    myTicketsViewModel: MyTicketsViewModel, // NEW
    profileViewModel: ProfileViewModel      // NEW
) {
    val navController = rememberNavController()

    val ticket by ticketViewModel.ticket.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.statusBarsPadding()
    ) {

        composable("home") {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        navController.navigate("explore")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Explore Services")
                }

                // NEW: buttons to My Tickets and Profile
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.navigate("my_tickets") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("My Tickets")
                    }
                    OutlinedButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Profile")
                    }
                }

                if (ticket != null) {
                    Button(
                        onClick = {
                            navController.navigate("ticket")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("My Active Ticket #${ticket?.ticketNumber}")
                    }
                }

                HomeRoute(
                    viewModel = homeViewModel,
                    onServiceClick = { serviceId ->
                        navController.navigate("details/$serviceId")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        composable("explore") {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Text("Back to Home")
                }

                ExploreRoute(
                    viewModel = exploreViewModel
                )
            }
        }

        composable(
            route = "details/{serviceId}",
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val serviceId =
                backStackEntry.arguments?.getString("serviceId")

            val service = serviceId?.let {
                AppContainer.serviceRepository.getServiceById(it)
            }

            ServiceDetailsScreen(
                service = service,
                ticket = ticket,
                onBackClick = {
                    navController.navigateUp()
                },
                onJoinQueueClick = {
                    if (service != null) {
                        val result = ticketViewModel.joinQueue(service)

                        if (result == JoinQueueResult.SUCCESS) {
                            navController.navigate("ticket")
                        }
                    }
                },
                onViewTicketClick = {
                    navController.navigate("ticket")
                }
            )
        }

        composable("ticket") {

            val service = ticket?.serviceId?.let {
                AppContainer.serviceRepository.getServiceById(it)
            }

            TicketScreen(
                ticket = ticket,
                service = service,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        // NEW: My Tickets screen
        composable("my_tickets") {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Back")
                }
                MyTicketsRoute(
                    viewModel = myTicketsViewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // NEW: Profile screen
        composable("profile") {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Back")
                }
                ProfileRoute(
                    viewModel = profileViewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}