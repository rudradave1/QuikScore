package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Coach(
    val lineup_number: String,
    val lineup_player: String,
    val lineup_position: String,
    val player_key: String
) : Parcelable