package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(
            text = "Clock it",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 36.sp
            )
        },
        actions = { ->
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Reorder,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}