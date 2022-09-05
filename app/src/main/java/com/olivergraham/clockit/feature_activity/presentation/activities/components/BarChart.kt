package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData

@Composable
fun BarChartView(/*activities: List<Activity>*/) {
    val barList = listOf(
        BarChartData.Bar(
            label = "bar 1",
            value = 2f,
            color = Color.Blue
        ),
        BarChartData.Bar(
            label = "bar 2",
            value = 10f,
            color = Color.Green
        )

    )
    BarChart(
        barChartData = BarChartData(
            bars = barList
        ),
        /*barChartData = BarChartData(
            bars = activityBars(activities)
        )*/
        modifier = Modifier/*.fillMaxSize()*/
    )
}

private fun activityBars(activities: List<Activity>): List<BarChartData.Bar> {
    val barList = mutableListOf<BarChartData.Bar>()

    for (activity in activities) {
        barList.add(
            BarChartData.Bar(
                label = activity.name,
                value = activity.timeSpent.toFloat(),
                color = Color(activity.color)
            )
        )
    }

    return barList
}