package com.rudra.quikscore.util

import com.rudra.quikscore.model.Away
import com.rudra.quikscore.model.Card
import com.rudra.quikscore.model.Goalscorer
import com.rudra.quikscore.model.Home
import com.rudra.quikscore.model.Lineup
import com.rudra.quikscore.model.MatchesItem
import com.rudra.quikscore.model.Statistic
import com.rudra.quikscore.model.Statistics1half
import com.rudra.quikscore.model.Substitutions
import java.util.UUID

fun generateRandomMatch(): MatchesItem {
    val random = java.util.Random()

    // Generate random number of cards
    val cards = List(random.nextInt(5)) {
        Card(
            time = "${random.nextInt(90)}'",
            home_fault = "Home Player ${random.nextInt(11)}",
            card = if (random.nextBoolean()) "Yellow Card" else "Red Card",
            away_fault = "Away Player ${random.nextInt(11)}",
            info = ""
        )
    }

    // Generate random number of goalscorers
    val goalscorers = List(random.nextInt(5)) {
        Goalscorer(
            away_assist = "",
            away_assist_id = "",
            away_scorer = "Away Player ${random.nextInt(11)}",
            away_scorer_id = "",
            home_assist = "",
            home_assist_id = "",
            home_scorer = "Home Player ${random.nextInt(11)}",
            home_scorer_id = "",
            info = "",
            score = "${random.nextInt(5)} - ${random.nextInt(3)}",
            score_info_time = "${random.nextInt(90)}'",
            time = ""
        )
    }

    // Generate random lineup
    val lineup = Lineup(
        away = Away(emptyList(), emptyList(), emptyList(), emptyList()),
        home = Home(emptyList(), emptyList(), emptyList(), emptyList())
    )

    // Generate random statistics
    val statistics = List(random.nextInt(5)) {
        Statistic(
            away = "${random.nextInt(10)}",
            home = "${random.nextInt(10)}",
            type = "Statistic Type ${random.nextInt(5)}"
        )
    }

    // Generate random statistics for first half
    val statistics1half = List(random.nextInt(3)) {
        Statistics1half(
            away = "${random.nextInt(5)}",
            home = "${random.nextInt(5)}",
            type = "Statistic Type ${random.nextInt(3)}"
        )
    }

    // Generate random substitutions
    val substitutions = Substitutions(
        away = List(random.nextInt(3)) {
            Away(emptyList(), emptyList(), emptyList(), emptyList())
        },
        home = List(random.nextInt(3)) {
            Home(emptyList(), emptyList(), emptyList(), emptyList())
        }
    )

    // Generate random match details
    return MatchesItem(
        cards = cards,
        country_id = "1",
        country_logo = "https://picsum.photos/200",
        country_name = "Country Name",
        fk_stage_key = "Stage Key",
        goalscorer = goalscorers,
        league_id = "1",
        league_logo = "https://picsum.photos/200",
        league_name = "League Name",
        league_year = "2024",
        lineup = lineup,
        match_awayteam_extra_score = "0",
        match_awayteam_ft_score = "0",
        match_awayteam_halftime_score = "0",
        match_awayteam_id = "1",
        match_awayteam_name = "Away Team",
        match_awayteam_penalty_score = "0",
        match_awayteam_score = "0",
        match_awayteam_system = "4-4-2",
        match_date = "2024-06-22",
        match_hometeam_extra_score = "0",
        match_hometeam_ft_score = "0",
        match_hometeam_halftime_score = "0",
        match_hometeam_id = "1",
        match_hometeam_name = "Home Team",
        match_hometeam_penalty_score = "0",
        match_hometeam_score = "0",
        match_hometeam_system = "4-4-2",
        match_id = UUID.randomUUID().toString(),
        match_live = "1",
        match_referee = "Referee Name",
        match_round = "1",
        match_stadium = "Stadium Name",
        match_status = "Live",
        match_time = "15:00",
        stage_name = "Group Stage",
        statistics = statistics,
        statistics_1half = statistics1half,
        substitutions = substitutions,
        team_away_badge = "https://picsum.photos/200",
        team_home_badge = "https://picsum.photos/200"
    )
}
