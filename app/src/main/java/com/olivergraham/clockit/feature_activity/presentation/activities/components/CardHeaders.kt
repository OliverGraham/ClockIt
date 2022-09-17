package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.olivergraham.clockit.feature_activity.domain.model.Activity


// TODO: Refactor this file

@Composable
fun CardHeader(
    activity: Activity,
    deleteActivity: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {
    if (activity.name.length < 15) {
        ShortTextCardHeader(
            activity = activity,
            deleteActivity = deleteActivity,
            navigateWithActivity = navigateWithActivity
        )
    } else {
        LongTextCardHeader(
            activity = activity,
            deleteActivity = deleteActivity,
            navigateWithActivity = navigateWithActivity
        )
    }
}

@Composable
private fun LongTextCardHeader(
    activity: Activity,
    deleteActivity: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {
    val expanded = remember { mutableStateOf(value = false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary),
        horizontalArrangement = Arrangement.SpaceBetween,
        //verticalAlignment = Alignment.CenterVertically
    ) { ->
        Text(
            //modifier = Modifier.align(Alignment.Center),
            modifier = Modifier.weight(weight = 3f),
            text = activity.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        //Spacer(Modifier.weight(1f))
        IconButton(
            modifier = Modifier.weight(weight = 1f),
            //modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp),
            onClick = { expanded.value = true }
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options button")

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
            ) {
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) { ->
                    DropdownMenuItem(
                        text = { Text(text = "Edit Activity") },
                        onClick = { navigateWithActivity(activity) }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete Activity") },
                        onClick = {
                            deleteActivity(activity)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShortTextCardHeader(
    activity: Activity,
    deleteActivity: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {
    val expanded = remember { mutableStateOf(value = false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) { ->
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = activity.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(8.dp),
            onClick = { expanded.value = true }
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options button")

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
            ) {
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) { ->
                    DropdownMenuItem(
                        text = { Text(text = "Edit Activity") },
                        onClick = { navigateWithActivity(activity) }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete Activity") },
                        onClick = {
                            deleteActivity(activity)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}