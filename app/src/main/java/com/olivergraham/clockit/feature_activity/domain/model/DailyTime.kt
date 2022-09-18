package com.olivergraham.clockit.feature_activity.domain.model

import java.time.LocalDateTime

data class DailyTime(
    val timeSpent: Long = 0L,
    // val date: String
    val date: LocalDateTime?
)
