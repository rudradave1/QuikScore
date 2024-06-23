package com.rudra.quikscore.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rudra.quikscore.R
import com.rudra.quikscore.floatingwindow.ComposeFloatingWindow
import com.rudra.quikscore.floatingwindow.DialogPermission
import com.rudra.quikscore.model.Goalscorer
import com.rudra.quikscore.model.Lineup
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.model.Statistic
import com.rudra.quikscore.presentation.component.LiveScoreBubble
import com.rudra.quikscore.presentation.component.MyTopAppBar
import com.rudra.quikscore.presentation.component.PinScoreButton
import com.rudra.quikscore.presentation.viewmodel.DetailedMatchViewModel
import com.rudra.quikscore.util.UiState

@Composable
fun MatchDetailsScreen(
    matchId: String,
    isDemo: Boolean,
    viewModel: DetailedMatchViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Fetch match details when matchId or isDemo changes
    LaunchedEffect(key1 = matchId, key2 = isDemo) {
        viewModel.fetchMatch(matchId, isDemo)
    }

    // Observe match details and UI state using collectAsState
    val uiState by viewModel.uiState.collectAsState()

    // Handle permission dialog state
    val showDialogPermission = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(id = R.string.match_details),
                navigationIcon = R.drawable.baseline_arrow_back_24,
                onNavigationIconClick = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Loaded -> {
                val match = (uiState as? UiState.Loaded<MatchesItem>)?.data
                match?.let { loadedMatch ->
                    val floatingWindow =
                        ComposeFloatingWindow(context = LocalContext.current.applicationContext)

                    floatingWindow.setContent {
                        LiveScoreBubble(
                            match = match,
                            onCloseClick = {
                                floatingWindow.hide()
                            },
                            onClick = {
                                Log.d(
                                    "QuickScore",
                                    "window Clicked: $loadedMatch"
                                )
                            }
                        )
                    }
                    MatchDetailsContent(it, loadedMatch, showDialogPermission, floatingWindow)
                }

            }

            is UiState.Error -> {
                val errorText = (uiState as UiState.Error).error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorText.asString(), style = MaterialTheme.typography.h6)
                }
            }

        }
    }
}

@Composable
fun MatchDetailsContent(
    padding: PaddingValues,
    loadedMatch: MatchesItem,
    showDialogPermission: MutableState<Boolean>,
    floatingWindow: ComposeFloatingWindow,
) {

    DialogPermission(showDialogState = showDialogPermission)


    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp, start = 8.dp, end = 8.dp),
    ) {
        item { MatchHeader(loadedMatch) }
        item {
            // Pin Score Button
            PinScoreButton {
                if (floatingWindow.isAvailable()) {
                    if (floatingWindow.decorView.childCount != 0) {
                        floatingWindow.show()
                    }
                } else {
                    // Handle dialog or toast for permission issue
                    showDialogPermission.value = true
                    Log.d(
                        "QuickScore",
                        "PinScoreButton clicked for match: $loadedMatch"
                    )
                }
            }
        }
        item { MatchDetailsSection(loadedMatch) }
        item { MatchScoresSection(loadedMatch) }
        item { TeamBadgesSection(loadedMatch) }
        if (loadedMatch.goalscorer.isNotEmpty()) {
            item { GoalscorersSection(loadedMatch.goalscorer) }
        }
        item { LineupSection(loadedMatch.lineup) }
        if (loadedMatch.statistics.isNotEmpty()) {
            item { StatisticsSection(loadedMatch.statistics) }
        }

    }
}


@Composable
private fun MatchHeader(match: MatchesItem) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Match ID: ${match.match_id ?: "N/A"}", style = MaterialTheme.typography.h6)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                match.league_logo?.let { url ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "League Logo",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Column {
                    Text(
                        "League: ${match.league_name ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        "Stage: ${match.stage_name ?: "N/A"}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                match.country_logo?.let { url ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Country Logo",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    "Country: ${match.country_name ?: "N/A"}",
                    style = MaterialTheme.typography.body1
                )
            }
            Text("Date: ${match.match_date ?: "N/A"}", style = MaterialTheme.typography.body1)
            Text("Time: ${match.match_time ?: "N/A"}", style = MaterialTheme.typography.body1)
            Text("Status: ${match.match_status ?: "N/A"}", style = MaterialTheme.typography.body1)
            Text("Stadium: ${match.match_stadium ?: "N/A"}", style = MaterialTheme.typography.body1)
            if (match.match_referee?.isNotEmpty() == true) {
                Text("Referee: ${match.match_referee}", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
private fun MatchDetailsSection(match: MatchesItem) {
    if (match.match_round.isNotEmpty() || match.match_live.isNotEmpty()) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (match.match_round.isNotEmpty()) {
                    Text(
                        "Match Round: ${match.match_round}",
                        style = MaterialTheme.typography.body1
                    )
                }
                if (match.match_live.isNotEmpty()) {
                    Text(
                        "Live Status: ${if (match.match_live == "1") "Live" else "Not Live"}",
                        style = MaterialTheme.typography.body1
                    )
                }

                // Display team live scores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${match.match_hometeam_name}: ${match.match_hometeam_score}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${match.match_awayteam_name}: ${match.match_awayteam_score}",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


@Composable
private fun MatchScoresSection(match: MatchesItem) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Full Time Scores:", style = MaterialTheme.typography.h6)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "${match.match_hometeam_name}: ${match.match_hometeam_ft_score ?: "N/A"}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    "${match.match_awayteam_name}: ${match.match_awayteam_ft_score ?: "N/A"}",
                    style = MaterialTheme.typography.body1
                )
            }
            Text("Halftime Scores:", style = MaterialTheme.typography.h6)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "${match.match_hometeam_name}: ${match.match_hometeam_halftime_score ?: "N/A"}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    "${match.match_awayteam_name}: ${match.match_awayteam_halftime_score ?: "N/A"}",
                    style = MaterialTheme.typography.body1
                )
            }
            if (match.match_hometeam_extra_score?.isNotEmpty() == true || match.match_awayteam_extra_score?.isNotEmpty() == true) {
                Text("Extra Time Scores:", style = MaterialTheme.typography.h6)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "${match.match_hometeam_name}: ${match.match_hometeam_extra_score ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        "${match.match_awayteam_name}: ${match.match_awayteam_extra_score ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            if (match.match_hometeam_penalty_score?.isNotEmpty() == true || match.match_awayteam_penalty_score?.isNotEmpty() == true) {
                Text("Penalty Scores:", style = MaterialTheme.typography.h6)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "${match.match_hometeam_name}: ${match.match_hometeam_penalty_score ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        "${match.match_awayteam_name}: ${match.match_awayteam_penalty_score ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamBadgesSection(match: MatchesItem) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                match.team_home_badge?.let { TeamBadge(url = it) }
                Text(match.match_hometeam_name ?: "N/A", style = MaterialTheme.typography.body1)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                match.team_away_badge?.let { TeamBadge(url = it) }
                Text(match.match_awayteam_name ?: "N/A", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
private fun GoalscorersSection(goalscorers: List<Goalscorer>) {
    if (goalscorers.isNotEmpty()) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Goalscorers:", style = MaterialTheme.typography.h6)
                for (goalscorer in goalscorers) {
                    Text(
                        text = "Time: ${goalscorer.time}, Scorer: ${goalscorer.away_scorer ?: goalscorer.home_scorer ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
private fun LineupSection(lineup: Lineup) {
    if (lineup.home.starting_lineups.isNotEmpty() || lineup.away.starting_lineups.isNotEmpty()) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Lineups:", style = MaterialTheme.typography.h6)
                if (lineup.home.starting_lineups.isNotEmpty()) {
                    Text("Home Team Lineup", style = MaterialTheme.typography.h6)
                    Text(
                        "Starting Lineups: ${lineup.home.starting_lineups.joinToString(", ") { it.toString() }}",
                        style = MaterialTheme.typography.body1
                    )
                    if (lineup.home.substitutes.isNotEmpty()) {
                        Text(
                            "Substitutes: ${lineup.home.substitutes.joinToString(", ") { it.toString() }}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    if (lineup.home.coach.isNotEmpty()) {
                        Text("Coach: ${lineup.home.coach}", style = MaterialTheme.typography.body1)
                    }
                    if (lineup.home.missing_players.isNotEmpty()) {
                        Text(
                            "Missing Players: ${lineup.home.missing_players.joinToString(", ") { it.toString() }}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                if (lineup.away.starting_lineups.isNotEmpty()) {
                    Text("Away Team Lineup", style = MaterialTheme.typography.h6)
                    Text(
                        "Starting Lineups: ${lineup.away.starting_lineups.joinToString(", ") { it.toString() }}",
                        style = MaterialTheme.typography.body1
                    )
                    if (lineup.away.substitutes.isNotEmpty()) {
                        Text(
                            "Substitutes: ${lineup.away.substitutes.joinToString(", ") { it.toString() }}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                    if (lineup.away.coach.isNotEmpty()) {
                        Text("Coach: ${lineup.away.coach}", style = MaterialTheme.typography.body1)
                    }
                    if (lineup.away.missing_players.isNotEmpty()) {
                        Text(
                            "Missing Players: ${lineup.away.missing_players.joinToString(", ") { it.toString() }}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsSection(statistics: List<Statistic>) {
    if (statistics.isNotEmpty()) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Statistics:", style = MaterialTheme.typography.h6)
                for (statistic in statistics) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "${statistic.type}: Home ${statistic.home ?: "0"} - Away ${statistic.away ?: "0"}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamBadge(url: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = "Team Badge",
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.surface)
    )
}
