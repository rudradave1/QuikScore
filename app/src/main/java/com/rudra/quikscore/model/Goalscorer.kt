package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Goalscorer(
    val away_assist: String,
    val away_assist_id: String,
    val away_scorer: String,
    val away_scorer_id: String,
    val home_assist: String,
    val home_assist_id: String,
    val home_scorer: String,
    val home_scorer_id: String,
    val info: String,
    val score: String,
    val score_info_time: String,
    val time: String
) : Parcelable