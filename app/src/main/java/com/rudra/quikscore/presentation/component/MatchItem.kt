package com.rudra.quikscore.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rudra.quikscore.R
import com.rudra.quikscore.model.MatchesItem


@Composable
fun MatchItem(match: MatchesItem, onClick: (MatchesItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(match) },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Team Section
            TeamSection(
                teamName = match.match_hometeam_name,
                teamBadge = match.team_home_badge,
                score = match.match_hometeam_score.toIntOrNull() ?: 0,
                isLive = match.match_live == "1"
            )

            // Match Status and Time Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (match.match_live == "1") stringResource(id = R.string.live) else "",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = match.match_time,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Away Team Section
            TeamSection(
                teamName = match.match_awayteam_name,
                teamBadge = match.team_away_badge,
                score = match.match_awayteam_score.toIntOrNull() ?: 0,
                isLive = match.match_live == "1"
            )
        }
    }
}

@Composable
private fun TeamSection(
    teamName: String,
    teamBadge: String,
    score: Int,
    isLive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(teamBadge)
                .crossfade(true)
                .build(),
            contentDescription = teamName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1,
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (isLive) {
            Text(
                text = stringResource(id = R.string.live),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}