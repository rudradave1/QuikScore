package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Lineup(
    val away: Away,
    val home: Home
) : Parcelable
