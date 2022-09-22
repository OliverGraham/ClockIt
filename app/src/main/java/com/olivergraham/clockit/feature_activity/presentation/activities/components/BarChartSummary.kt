package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer


@Composable
fun BarChartSummary(activity: Activity, maxBarValue: Float) {

    val barList = recentDailyTimeBars(activity = activity)

    BarChart(
        barChartData = BarChartData(
            bars = barList,
            padBy = 5f,
            startAtZero = true,
            maxBarValue = maxBarValue
        ),
        modifier = Modifier/*.fillMaxSize()*/,
        labelDrawer = SimpleLabelDrawer(
            drawLocation = SimpleLabelDrawer.DrawLocation.XAxis
        )
    )
}

/** Show 0 to 2 bars, determined by number of dailyTimes in the given Activity  */
@Composable
private fun recentDailyTimeBars(activity: Activity): List<BarChartData.Bar> {

    val size = activity.dailyTimes.size
    if (size == 0) {
        return listOf(
            emptyBar()
        )
    }

    val todayDailyTime = activity.dailyTimes[size - 1]
    val todayBarColor = remember { mutableStateOf(value = activity.getRandomNonBackgroundColor()) }
    if (size == 1) {
        return listOf(
            barFromDailyTime(todayDailyTime, todayBarColor.value)
        )
    }

    val beforeTodayDailyTime = activity.dailyTimes[size - 2]
    val beforeTodayBarColor  = remember {
        mutableStateOf(value = activity.getRandomNonBackgroundColor(todayBarColor.value))
    }
    return listOf(
        barFromDailyTime(beforeTodayDailyTime, beforeTodayBarColor.value),
        barFromDailyTime(todayDailyTime, todayBarColor.value)
    )
}





// TODO: label as Today || Fri, 29
//       label as Yesterday || Thurs, 28
//       It needs to read by date, not by index of DailyTimes in the list -> right now
//       it puts the latest (size - 1) index as "Today" even though
//       (at this time) it's actually yesterday
//       FIX!: Today will always be whatever today actually is (and start empty)
//             the day to the left will either "yesterday" OR whatever the most recent date is
//             (Fri, 9/19)

private fun determineXAxisLabel() {

}

// TODO: is it hours? Seconds? Days?
private fun determineYAxisLabel(maxBarValue: Float) {

}

/** Return a specified bar from the given DailyTime and a color */
private fun barFromDailyTime(dailyTime: DailyTime, barColor: Int) = BarChartData.Bar(
    label = dailyTime.monthDay(),
    value = dailyTime.timeSpent.toFloat(),
    color = Color(color = barColor)
)

/** Return an empty bar */
private fun emptyBar(label: String = "Today") = BarChartData.Bar(
    label = label,
    value = 0f,
    color = Color(color = 0)
)