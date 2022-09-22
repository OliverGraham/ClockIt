package com.olivergraham.clockit.feature_activity.domain.model

import java.time.LocalDateTime

data class DailyTime(
    val timeSpent: Long = 0L,
    val date: LocalDateTime?
) {

    /** Return the date's month/day as a string*/
    fun monthDay(): String {
        val theDate = date?.toLocalDate()
        return "${theDate?.monthValue}/${theDate?.dayOfMonth}"
    }

    companion object {
        fun areDatesEqual(lhs: LocalDateTime?, rhs: LocalDateTime?): Boolean {
            if (lhs == null || rhs == null) {
                return false
            }
            return lhs == rhs
        }
    }
}
