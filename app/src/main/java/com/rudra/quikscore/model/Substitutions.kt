package com.rudra.quikscore.model

import android.os.Parcelable
 
import kotlinx.parcelize.Parcelize

 
@Parcelize
data class Substitutions(
    val away: List<Away>,
    val home: List<Home>
) : Parcelable