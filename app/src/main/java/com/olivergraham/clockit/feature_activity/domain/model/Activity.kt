package com.olivergraham.clockit.feature_activity.domain.model

import com.olivergraham.clockit.feature_activity.common.Dates
import com.olivergraham.clockit.ui.theme.*

/**
 * mostRecentClockIn will be a string in this format: DateTimeFormatter.ISO_DATE_TIME
 * */
data class Activity(
    val name: String = "Unnamed Activity",
    val color: Int = 0,
    val isClockedIn: Boolean = false,
    val mostRecentClockIn: String = "",
    val timeSpent: Long = 0L,
    // val timeSpentPerDay: List<Long> = emptyList(),
    val id: Int? = null
) {
    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }

    // TODO: need time spent per day, to show as the Y axis in the chart
    //       need to save each day in a list...


    fun mostRecentClockInAsLabel() = Dates.dateStringToLabel(mostRecentClockIn)
    fun timeSpentAsLabel() = Dates.convertSecondsToLabel(timeSpent)
    // fun timeSpentPerDayAsLabel() = timeSpentPerDay.map { Dates.convertSecondsToLabel(it) }
}

class InvalidActivityException(message: String): Exception(message)
