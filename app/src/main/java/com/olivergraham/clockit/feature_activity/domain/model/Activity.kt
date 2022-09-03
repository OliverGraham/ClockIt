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
    val id: Int? = null
) {
    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }

    fun mostRecentClockInAsLabel() = Dates.dateStringToLabel(mostRecentClockIn)
    fun timeSpentAsLabel() = Dates.convertSecondsToLabel(timeSpent)
}

class InvalidActivityException(message: String): Exception(message)
