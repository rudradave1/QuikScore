package com.rudra.quikscore.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MyTopAppBar(
    title: String,
    navigationIcon: Int? = null,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = Color.White
            )
        },
        navigationIcon = {
            navigationIcon?.let {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            Row {
                actions()
            }
        },
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        elevation = 4.dp,
    )
}
