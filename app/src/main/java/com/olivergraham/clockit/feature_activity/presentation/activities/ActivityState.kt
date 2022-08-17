package com.olivergraham.clockit.feature_activity.presentation.activities

import com.olivergraham.clockit.feature_activity.domain.utility.getCurrentDateTime
import java.util.Date

data class ActivityState(
    val name: String = "Unnamed Activity",
    val timeSpent: Date = getCurrentDateTime()
)
