package com.rudra.quikscore.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun TeamSection(teamName: String, teamBadge: String, teamScore: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(teamBadge)
                .crossfade(true)
                .build(),
            contentDescription = teamName,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shadow(4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f) // Expand to fill available horizontal space
                .padding(end = 8.dp) // Add padding between badge and text
        ) {
            Text(
                text = teamName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1, // Ensure only one line for team name
                overflow = TextOverflow.Ellipsis // Truncate with ellipsis if too long
            )
            Text(
                text = teamScore,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1, // Ensure only one line for team score
                overflow = TextOverflow.Ellipsis // Truncate with ellipsis if too long
            )
        }
    }
}