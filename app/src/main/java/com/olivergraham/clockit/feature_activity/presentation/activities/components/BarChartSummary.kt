package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime
import com.olivergraham.clockit.feature_activity.utility.TimeLabels
import com.olivergraham.clockit.ui.theme.getFontSize
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer


@Composable
fun BarChartSummary(activity: Activity) {

    // re-run this calculation only if today's time has changed
    val barInfo = if (activity.dailyTimes.size > 0) {
        remember(activity.dailyTimes[activity.dailyTimes.lastIndex].timeSpent) {
            mutableStateOf(getMaxBar(activity = activity)).value
        }
    } else {
        BarChartInfo()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
    ) { ->
        Text(text = barInfo.timeUnitLabel)
        Spacer(modifier = Modifier.padding(16.dp))
        BarChartBody(
            activity = activity,
            maxBarValue = barInfo.maxBarValue,
            timeModifier = barInfo.timeModifier
        )
    }
}


@Composable
private fun BarChartBody(activity: Activity, maxBarValue: Float, timeModifier: Float) {

    BarChart(
        barChartData = BarChartData(
            bars = recentDailyTimeBars(activity = activity, timeModifier = timeModifier),
            padBy = 5f,
            startAtZero = true,
            maxBarValue = maxBarValue
        ),
        modifier = Modifier/*.fillMaxSize()*/,
        xAxisDrawer = SimpleXAxisDrawer(
            axisLineThickness = 2.dp
        ),
        yAxisDrawer = SimpleYAxisDrawer(
            labelTextSize = MaterialTheme.typography.getFontSize() / 1.2,
            axisLineThickness = 2.dp
        ),
        labelDrawer = SimpleLabelDrawer(
            drawLocation = SimpleLabelDrawer.DrawLocation.XAxis,
            labelTextSize = MaterialTheme.typography.getFontSize() / 1.2
        )
    )
}

/** Show 0 to 2 bars, determined by number of dailyTimes in the given Activity  */
@Composable
private fun recentDailyTimeBars(activity: Activity, timeModifier: Float): List<BarChartData.Bar> {

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
            barFromDailyTime(todayDailyTime, timeModifier, todayBarColor.value)
        )
    }

    val beforeTodayDailyTime = activity.dailyTimes[size - 2]
    val beforeTodayBarColor = remember {
        mutableStateOf(value = activity.getRandomNonBackgroundColor(todayBarColor.value))
    }
    return listOf(
        barFromDailyTime(beforeTodayDailyTime, timeModifier, beforeTodayBarColor.value),
        barFromDailyTime(todayDailyTime, timeModifier, todayBarColor.value)
    )
}

/**  Triple<Float, Float, String> */
private fun getMaxBar(activity: Activity): BarChartInfo {

    val size = activity.dailyTimes.size
    if (size == 0) {
        return BarChartInfo()
    }

    if (size == 1) {
        return BarChartInfo.tripleToBarChartInfo(TimeLabels.barChartYAxis(activity.dailyTimes[0]))
    }

    // two most recent days
    val mostRecentDailyTime = activity.dailyTimes[size - 1]
    val nextMostRecentDailyTime = activity.dailyTimes[size - 2]

    if (mostRecentDailyTime.timeSpent > nextMostRecentDailyTime.timeSpent) {
        return BarChartInfo.tripleToBarChartInfo(TimeLabels.barChartYAxis(mostRecentDailyTime))
    }

    return BarChartInfo.tripleToBarChartInfo(TimeLabels.barChartYAxis(nextMostRecentDailyTime))
}

/** Return a specified bar from the given DailyTime and a color */
private fun barFromDailyTime(dailyTime: DailyTime, timeModifier: Float, barColor: Int) =
    BarChartData.Bar(
        label = dailyTime.monthDay(),
        value = dailyTime.timeSpent / timeModifier,
        color = Color(color = barColor)
)

/** Return an empty bar */
private fun emptyBar(label: String = "Today") = BarChartData.Bar(
    label = label,
    value = 0f,
    color = Color(color = 0)
)

/** Provide labels and other information for the bar chart */
private data class BarChartInfo(
    val timeModifier: Float = 1F,
    val maxBarValue: Float = 60F,
    val timeUnitLabel: String = "Seconds"
) {
    companion object {
        fun tripleToBarChartInfo(barInfo: Triple<Float, Float, String>): BarChartInfo {
            return BarChartInfo(
                timeModifier = barInfo.first,
                maxBarValue = barInfo.second,
                timeUnitLabel = barInfo.third
            )
        }
    }
}