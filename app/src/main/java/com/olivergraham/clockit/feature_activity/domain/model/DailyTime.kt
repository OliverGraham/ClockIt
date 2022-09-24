package com.olivergraham.clockit.feature_activity.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyTime(
    val timeSpent: Long = 0L,
    val date: LocalDateTime?
) {

    /** Return the date's month/day as a string */
    fun monthDay(): String {
        val theDate = date?.toLocalDate()
        val fullDate = "${theDate?.monthValue}/${theDate?.dayOfMonth}"
        return if (theDate == LocalDate.now()) "Today" else fullDate
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
