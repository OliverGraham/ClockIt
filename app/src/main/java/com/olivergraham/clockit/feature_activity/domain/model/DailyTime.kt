package com.olivergraham.clockit.feature_activity.domain.model

import java.time.LocalDateTime

data class DailyTime(
    val timeSpent: Long = 0L,
    val date: LocalDateTime?
) {
    fun LocalDateTime.asDateOnly() = date?.toLocalDate()
}
