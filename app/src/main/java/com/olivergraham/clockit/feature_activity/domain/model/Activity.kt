package com.olivergraham.clockit.feature_activity.domain.model

import com.olivergraham.clockit.ui.theme.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


data class Activity(
    val name: String = "",
    val color: Int = 0,
    val isClockedIn: Boolean = false,
    val lastClockIn: LocalDateTime? = null,
    val timeSpent: Long = 0L,           // TODO: change to totalTimeSpent
    val dailyTimes: MutableList<DailyTime> = mutableListOf(),
    val id: Int? = null
) {

    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)

        // Label in this format:    Sep 16, 2022, 8:16:10 PM
        private fun LocalDateTime.toLabel(): String = this.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        )

        private const val secondsInDay = 86400L
        private const val secondsInHour = 3600L
        private const val secondsInMinute = 60L

        private fun Long.toDays(): Long = this / secondsInDay
        private fun Long.toHours(): Long = this / secondsInHour
        private fun Long.toMinutes(): Long = this / secondsInMinute

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
    }

    fun lastClockInLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to get started!"
        }
        return "Last clock in:\n${lastClockIn.toLabel()}"
    }


    fun getCurrentDate(): LocalDateTime = LocalDateTime.now()

    /** Copies and returns given activity, after adjusting some clock in fields */
    fun clockIn(): Activity {
        // appendDailyTime()
        // dailyTimes.add(DailyTime(date =))
        return copy(
            lastClockIn =  LocalDateTime.now(),
            isClockedIn = true
        )
    }

    // TODO: Save date of last clock out??
    /** Copies and returns given activity, after adjusting some clock out fields */
    fun clockOut(): Activity = copy(
        timeSpent = calculateTimeSpent(),
        isClockedIn = false
    )

    private fun calculateTimeSpent(): Long =
        Duration.between(lastClockIn, LocalDateTime.now()).seconds + timeSpent

    fun timeSpentLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to track time spent"
        }
        if (timeSpent == 0L) {
            return "Tracking time..."
        }
        return "Total time spent:\n${convertSecondsToLabel(timeSpent)}"
    }


    /**  -----------DailyTime-----------  **/

    /*fun appendDailyTime() {
        if (dailyTimes.isEmpty()) {
            dailyTimes.add(DailyTime(date = mostRecentClockInAsLabel()))
            return
        }
    }

    fun addToDailyTime(time: Long) {

        if (dailyTimes.isEmpty()) {
            dailyTimes.add(DailyTime(time, mostRecentClockInAsLabel()))
            return
        }
        val current = dailyTimes[dailyTimes.lastIndex]

        if (dailyTimes[dailyTimes.lastIndex].date == mostRecentClockIn) {
            dailyTimes[dailyTimes.lastIndex] = current.copy(
                timeSpent = current.timeSpent + time
            )
        } else {
            // if here, that means need to create a new one?
            dailyTimes.add(DailyTime(time, mostRecentClockInAsLabel()))
        }
    }*/
}

class InvalidActivityException(message: String): Exception(message)
