package com.olivergraham.clockit.feature_activity.presentation.common_components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LargeButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = Modifier
            .size(width = 180.dp, height = 45.dp),
        enabled = enabled,
        onClick = { onClick() }
    ) { ->
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}