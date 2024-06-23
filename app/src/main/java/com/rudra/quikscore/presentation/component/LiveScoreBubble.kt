package com.rudra.quikscore.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rudra.quikscore.floatingwindow.LocalFloatingWindow
import com.rudra.quikscore.floatingwindow.dragFloatingWindow
import com.rudra.quikscore.model.MatchesItem

@Composable
fun LiveScoreBubble(
    match: MatchesItem?,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            modifier = modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .dragFloatingWindow(),
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AnimatedVisibility(visible = match != null) {
                    match?.let { loadedMatch ->
                        // Header Row: Team Names and Live Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Home Team Info
                            TeamInfo(
                                teamBadgeUrl = loadedMatch.team_home_badge,
                                teamName = loadedMatch.match_hometeam_name,
                                teamScore = (loadedMatch.match_hometeam_score.toIntOrNull()
                                    ?: 0).toString(),
                                modifier = Modifier.weight(1f)
                            )

                            // Live Indicator and Match Time Label
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (loadedMatch.match_live == "1") {
                                    Text(
                                        text = "Live",
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                GameTimeText(
                                    time = "${loadedMatch.match_time}'",
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                )
                            }

                            // Away Team Info
                            TeamInfo(
                                teamBadgeUrl = loadedMatch.team_away_badge,
                                teamName = loadedMatch.match_awayteam_name,
                                teamScore = (loadedMatch.match_awayteam_score.toIntOrNull()
                                    ?: 0).toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } ?: run {
                    // Show message if no match data
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No match data", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        val floatingWindow = LocalFloatingWindow.current
        // Close Button Positioned Outside the Main Bubble
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = -8.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.6f))
                .clickable {
                    onCloseClick()
                    floatingWindow.hide()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


@Composable
fun TeamInfo(
    teamBadgeUrl: String,
    teamName: String,
    teamScore: String?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(teamBadgeUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Team Badge",
            modifier = Modifier.size(48.dp),  // Smaller image size
            contentScale = ContentScale.Crop
        )

        Text(
            text = teamName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = teamScore ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
