package com.olivergraham.clockit.domain.model

import com.olivergraham.clockit.utilities.getCurrentDateTime
import java.util.Date

data class Activity(
    val name: String = "Unnamed Activity",
    val timeSpent: Date = getCurrentDateTime()
)
