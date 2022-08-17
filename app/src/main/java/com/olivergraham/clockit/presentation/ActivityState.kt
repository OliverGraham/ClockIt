package com.olivergraham.clockit.presentation

import com.olivergraham.clockit.utilities.getCurrentDateTime
import java.util.Date

data class ActivityState(
    val name: String = "Unnamed Activity",
    val timeSpent: Date = getCurrentDateTime()
)
