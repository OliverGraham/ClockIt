package com.olivergraham.clockit.feature_activity.domain.model

import com.olivergraham.clockit.ui.theme.*

data class Activity(
    val name: String = "Unnamed Activity",
    // val timeSpent: Date = getCurrentDateTime(),
    val color: Int,
    val isClockedIn: Boolean = false,
    val mostRecentClockIn: String = "",
    val timeSpent: Double = 0.0,
    val id: Int? = null
) {
    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidActivityException(message: String): Exception(message)
