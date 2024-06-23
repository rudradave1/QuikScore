package com.rudra.quikscore.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications


// Define your route constants
sealed class Screen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object LiveMatchesScreen :
        Screen("live_matches_screen", "Live Matches", Icons.Default.Notifications)

    object DemoGamesScreen : Screen("demo_games_screen", "Demo Games", Icons.Default.Build)
    object MatchDetailScreen :
        Screen("match_detail_screen/{matchId}/{isDemo}", "Match Detail", Icons.Default.Info) {
        fun createRoute(matchId: String, isDemo: Boolean) = "match_detail_screen/$matchId/$isDemo"
    }
}