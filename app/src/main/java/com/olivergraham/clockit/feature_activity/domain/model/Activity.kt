package com.olivergraham.clockit.feature_activity.domain.model

import com.olivergraham.clockit.feature_activity.domain.utility.getCurrentDateTime
import com.olivergraham.clockit.ui.theme.*
import java.util.Date

data class Activity(
    val name: String = "Unnamed Activity",
    // val timeSpent: Date = getCurrentDateTime(),
    val color: Int,
    val id: Int? = null
) {
    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidActivityException(message: String): Exception(message)
