package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.presentation.common_components.LargeButton

@Composable
fun ActivityCardContent(
    activity: Activity,
    clockedInActivityId: Int?,
    clockIn: (activity: Activity) -> Unit,
    clockOut: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit,
    deleteActivity: (activity: Activity) -> Unit
) {
    CardHeader(
        activity = activity,
        deleteActivity = deleteActivity,
        navigateWithActivity = navigateWithActivity
    )

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
            .background(color = Color(activity.color))
    ) { ->
        Text(
            text = activity.lastClockInLabel(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = activity.timeSpentLabel(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        BarChartSummary(activity = activity)
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        Column { ->
            LargeButton(
                text = "Clock In",
                enabled = clockedInActivityId == null && !activity.isClockedIn,
                onClick = { clockIn(activity) }
            )
            Spacer(modifier = Modifier.padding(6.dp))
            LargeButton(
                text = "Clock Out",
                enabled = clockedInActivityId == activity.id || activity.isClockedIn,
                onClick = { clockOut(activity) }
            )
        }
    }
}