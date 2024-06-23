package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Away(
    val coach: List<Coach>,
    val missing_players: List<String>,
    val starting_lineups: List<Lineup>,
    val substitutes: List<Lineup>
) : Parcelable