package com.example.mobileprogramming.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobileprogramming.AppContainer
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
    myTicketsViewModel: MyTicketsViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()

    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val waitingCount = tickets.count { !it.isServed }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeRoute(
                viewModel = homeViewModel,
                waitingTicketsCount = waitingCount,
                onServiceClick = { serviceId -> navController.navigate("details/$serviceId") },
                onExploreClick = { navController.navigate("explore") },
                onTicketsClick = { navController.navigate("my_tickets") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable("explore") {
            ExploreRoute(
                viewModel = exploreViewModel,
                onBackClick = { navController.navigateUp() },
                onServiceClick = { serviceId -> navController.navigate("details/$serviceId") }
            )
        }

        composable(
            route = "details/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            val service = serviceId?.let { AppContainer.serviceRepository.getServiceById(it) }
            val waitingTicket = tickets.firstOrNull { it.serviceId == serviceId && !it.isServed }

            ServiceDetailsScreen(
                service = service,
                waitingTicket = waitingTicket,
                onBackClick = { navController.navigateUp() },
                onJoinQueueClick = {
                    if (service != null) {
                        ticketViewModel.joinQueue(service) { ticketId ->
                            navController.navigate("ticket/$ticketId")
                        }
                    }
                },
                onViewTicketClick = { ticketId -> navController.navigate("ticket/$ticketId") }
            )
        }

        composable(
            route = "ticket/{ticketId}",
            arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0
            val ticket = tickets.firstOrNull { it.id == ticketId }
            val service = ticket?.let { AppContainer.serviceRepository.getServiceById(it.serviceId) }

            TicketScreen(
                ticket = ticket,
                service = service,
                onBackClick = { navController.navigateUp() },
                onServedClick = myTicketsViewModel::markServed,
                onMyTicketsClick = { navController.navigate("my_tickets") }
            )
        }

        composable("my_tickets") {
            MyTicketsRoute(
                viewModel = myTicketsViewModel,
                onBackClick = { navController.navigateUp() },
                onTicketClick = { ticketId -> navController.navigate("ticket/$ticketId") }
            )
        }

        composable("profile") {
            ProfileRoute(
                viewModel = profileViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}