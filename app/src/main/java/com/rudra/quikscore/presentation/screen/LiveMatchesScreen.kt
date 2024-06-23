package com.rudra.quikscore.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rudra.quikscore.R
import com.rudra.quikscore.util.ErrorView
import com.rudra.quikscore.util.UiState
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.presentation.component.BottomNavigationBar
import com.rudra.quikscore.presentation.component.MatchItem
import com.rudra.quikscore.presentation.component.MyTopAppBar
import com.rudra.quikscore.presentation.navigation.Screen
import com.rudra.quikscore.presentation.viewmodel.MatchesViewModel
import com.rudra.quikscore.ui.theme.AppTheme

@Composable
fun LiveMatchesScreen(
    viewModel: MatchesViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val matches: List<MatchesItem> = when (val currentState = uiState) {
        is UiState.Loading -> {
            // Handle loading state
            emptyList() // or any other default value or behavior
        }
        is UiState.Error -> {
            // Handle error state
            // Example: Log the error or show a message
            emptyList() // or any other default value or behavior
        }
        is UiState.Loaded -> {
            // Handle loaded state
            currentState.data // Access the data safely
        }
    }
    val listState = rememberLazyListState()
    // Load more items when the user scrolls to the end of the list
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() &&
                    visibleItems.last().index >= matches.size - 1
                ) {
                    viewModel.fetchLiveMatches()
                }
            }
    }

    AppTheme {
        Scaffold(
            topBar = {
                MyTopAppBar(
                    title = "Live Matches",
                )
            },
            bottomBar = { BottomNavigationBar(navController = navHostController) },
        ) { paddingValues ->

            when (uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val error = (uiState as UiState.Error).error
                    ErrorView(
                        errorText = error,
                        action = { viewModel.fetchLiveMatches() }
                    )
                }

                is UiState.Loaded -> {
                    MatchesListScreen(
                        matches = matches,
                        paddingValues = paddingValues,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}


@Composable
private fun MatchesListScreen(
    matches: List<MatchesItem>,
    paddingValues: PaddingValues,
    navHostController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = matches,
            key = { index, match -> match.match_id } // Assuming match_id is unique and stable
        ) { index, match ->
            MatchItem(match = match, onClick = {
                navHostController.navigate(
                    Screen.MatchDetailScreen.createRoute(
                        match.match_id,
                        false
                    )
                )
            })
        }
    }
}
