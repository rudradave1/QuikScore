package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val time: String,
    val home_fault: String,
    val card: String,
    val away_fault: String,
    val info: String,
) : Parcelable
