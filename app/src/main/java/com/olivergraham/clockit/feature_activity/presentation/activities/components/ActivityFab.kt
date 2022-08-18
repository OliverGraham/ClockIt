package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ActivityFab(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add activity")
    }
}