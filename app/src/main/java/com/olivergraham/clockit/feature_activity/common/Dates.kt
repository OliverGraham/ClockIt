package com.olivergraham.clockit.feature_activity.common

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


private const val secondsInMinute = 60L
private const val secondsInHour = 3600L
private const val secondsInDay = 86400L

object Dates {
    fun getCurrentTime(): LocalDateTime = LocalDateTime.now()

    fun getCurrentTimeAsString(): String = dateToString(getCurrentTime())

    /** For printing only - not saving in data base */
    fun dateToString(dateTime: LocalDateTime): String = dateTime.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    )

    private fun stringToDate(dateString: String): LocalDateTime =
        LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

    fun dateStringToLabel(dateString: String): String {

        if (dateString.isEmpty()) {
            return ""
        }

        return dateToString(stringToDate(dateString))
    }

    fun calculateTimeSpent(startingDateString: String, timeSpent: Long) =
        Duration.between(stringToDate(startingDateString), LocalDateTime.now()).seconds + timeSpent


    fun convertSecondsToLabel(totalSeconds: Long): String {

        val days = totalSeconds.toDays()
        val hours = totalSeconds.toHours() - days * 24
        val minutes = totalSeconds.toMinutes() - totalSeconds.toHours() * 60
        val seconds = totalSeconds - totalSeconds.toMinutes() * 60

        return buildString { ->
            if (days > 0L) {
                if (days == 1L) {
                    append("$days day, ")
                } else {
                    append("$days days, ")
                }
            }
            if (hours > 0L) {
                if (hours == 1L) {
                    append("$hours hour, ")
                } else {
                    append("$hours hours, ")
                }
            }
            if (minutes > 0L) {
                if (minutes == 1L) {
                    append("$minutes minute, ")
                } else {
                    append("$minutes minutes, ")
                }
            }
            if (seconds > 0L) {
                if (seconds == 1L) {
                    append("$seconds second")
                } else {
                    append("$seconds seconds")
                }
            }
        }
    }

    private fun Long.toDays(): Long = this / secondsInDay
    private fun Long.toHours(): Long = this / secondsInHour
    private fun Long.toMinutes(): Long = this / secondsInMinute
}

