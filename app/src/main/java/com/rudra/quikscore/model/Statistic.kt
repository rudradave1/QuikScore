package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Statistic(
    val away: String,
    val home: String,
    val type: String
) : Parcelable