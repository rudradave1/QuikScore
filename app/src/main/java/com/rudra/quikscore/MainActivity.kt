package com.rudra.quikscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rudra.quikscore.presentation.component.BottomNavigationBar
import com.rudra.quikscore.presentation.navigation.Navigation
import com.rudra.quikscore.presentation.navigation.Screen
import com.rudra.quikscore.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            AppTheme {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = currentRoute != Screen.MatchDetailScreen.route,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                        ) {
                            if (currentRoute != Screen.MatchDetailScreen.route) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    }
                ) {
                    it
                    Navigation(
                        navController = navController,
                    )
                }
            }
        }
    }
}