package com.rudra.quikscore.presentation.component

import androidx.compose.runtime.Immutable
import com.rudra.quikscore.model.MatchesItem

@Immutable
data class OverlayScoreState(
    val homeTeamName: String,
    val awayTeamName: String,
    val homeScore: String,
    val awayScore: String,
    val matchTime: String,
    val isLive: Boolean
) {
    companion object {
        fun from(match: MatchesItem?): OverlayScoreState? {
            if (match == null) return null

            return OverlayScoreState(
                homeTeamName = match.match_hometeam_name,
                awayTeamName = match.match_awayteam_name,
                homeScore = match.match_hometeam_score.ifBlank { "0" },
                awayScore = match.match_awayteam_score.ifBlank { "0" },
                matchTime = match.match_time.ifBlank { "—" },
                isLive = match.match_live == "1"
            )
        }
    }
}
