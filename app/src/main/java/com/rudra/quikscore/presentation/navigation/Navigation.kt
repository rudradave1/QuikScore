package com.rudra.quikscore.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rudra.quikscore.presentation.screen.DemoGamesScreen
import com.rudra.quikscore.presentation.screen.LiveMatchesScreen
import com.rudra.quikscore.presentation.screen.MatchDetailsScreen


@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.DemoGamesScreen.route) {
        composable(Screen.DemoGamesScreen.route) {
            DemoGamesScreen(
                navHostController = navController
            )
        }
        composable(Screen.LiveMatchesScreen.route) {
            LiveMatchesScreen(
                navHostController = navController
            )
        }

        composable(
            route = Screen.MatchDetailScreen.route,
            arguments = listOf(
                navArgument("matchId") { type = NavType.StringType },
                navArgument("isDemo") { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: return@composable
            val isDemo = backStackEntry.arguments?.getBoolean("isDemo") ?: return@composable
            MatchDetailsScreen(
                matchId = matchId,
                isDemo = isDemo,
                navController = navController
            )
        }
    }
}

