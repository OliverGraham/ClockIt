package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Clock it") },
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