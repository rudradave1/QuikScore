package com.rudra.quikscore.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign


@Composable
fun GameTimeText(
    time: String,
    textStyle: TextStyle
) {
    Text(
        text = time,
        style = textStyle,
        textAlign = TextAlign.Center,
    )
}
