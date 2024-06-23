package com.rudra.quikscore.presentation.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rudra.quikscore.presentation.navigation.Screen


private val bottomNavigationItems = listOf(
    Screen.DemoGamesScreen,
    Screen.LiveMatchesScreen,
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavigationItems.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onSecondary,
                icon = { Icon(screen.icon, contentDescription = screen.title, tint = Color.Unspecified) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations on the back stack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when re-selecting it
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}