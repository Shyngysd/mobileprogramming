
package com.example.mobileprogramming.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.ui.details.ServiceDetailsScreen
import com.example.mobileprogramming.ui.explore.ExploreRoute
import com.example.mobileprogramming.ui.explore.ExploreViewModel
import com.example.mobileprogramming.ui.home.HomeRoute
import com.example.mobileprogramming.ui.home.HomeViewModel

@Composable
fun QueueNavigation(
    homeViewModel: HomeViewModel,
    exploreViewModel: ExploreViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.statusBarsPadding()
    ) {
        composable("home") {
            Column(modifier = Modifier.fillMaxSize()) {
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
            Column(modifier = Modifier.fillMaxSize()) {
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
                onBackClick = {
                    navController.navigateUp()
                },
                onJoinQueueClick = {
                }
            )
        }
    }
}
