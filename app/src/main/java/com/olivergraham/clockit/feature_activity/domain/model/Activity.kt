package com.olivergraham.clockit.feature_activity.domain.model

import com.olivergraham.clockit.feature_activity.domain.utility.getCurrentDateTime
import java.util.Date

data class Activity(
    val name: String = "Unnamed Activity",
    val timeSpent: Date = getCurrentDateTime()
) {
    companion object {
        val activityColors = listOf("")
    }
}

class InvalidActivityException(message: String): Exception(message)
