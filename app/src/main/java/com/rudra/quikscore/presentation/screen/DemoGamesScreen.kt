package com.rudra.quikscore.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rudra.quikscore.R
import com.rudra.quikscore.presentation.component.BottomNavigationBar
import com.rudra.quikscore.presentation.component.MatchItem
import com.rudra.quikscore.presentation.component.MyTopAppBar
import com.rudra.quikscore.presentation.navigation.Screen
import com.rudra.quikscore.presentation.viewmodel.MatchesViewModel
import com.rudra.quikscore.ui.theme.AppTheme
import com.rudra.quikscore.ui.theme.randomColorScheme

@Composable
fun DemoGamesScreen(
    viewModel: MatchesViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val matches by viewModel.demoMatches.collectAsState()
    AppTheme {
        Scaffold(
            topBar = {
                MyTopAppBar(
                    title = stringResource(id = R.string.demo_games),
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.fetchDemoMatches() }) {
                    Text(stringResource(id = (R.string.refresh)))
                }
            },
            bottomBar = { BottomNavigationBar(navController = navHostController) },
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = paddingValues
            ) {
                items(matches) { match ->
                    MatchItem(match) {
                        navHostController.navigate(
                            Screen.MatchDetailScreen.createRoute(
                                match.match_id,
                                true
                            ),
                        )
                    }
                }
            }
        }
    }
}
