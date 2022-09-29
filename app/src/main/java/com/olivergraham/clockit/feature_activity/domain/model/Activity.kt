package com.olivergraham.clockit.feature_activity.domain.model

import androidx.compose.ui.graphics.toArgb
import com.olivergraham.clockit.feature_activity.utility.TimeLabels
import com.olivergraham.clockit.ui.theme.*
import java.time.Duration
import java.time.LocalDateTime


data class Activity(
    val name: String = "",
    val color: Int = 0,
    val isClockedIn: Boolean = false,
    val lastClockIn: LocalDateTime? = null,
    val totalTimeSpent: Long = 0L,
    val dailyTimes: MutableList<DailyTime> = mutableListOf(),
    val id: Int? = null
) {

    override fun toString(): String {
        return name
    }

    /** Get a color that is distinct from the activity's main color */
    fun getRandomNonBackgroundColor(vararg takenColor: Int): Int {
        var randomColor = getRandomColor()
        while (randomColor == color || randomColor in takenColor) {
            randomColor = getRandomColor()
        }
        return randomColor
    }

    companion object {
        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
        fun getRandomColor(): Int = activityColors.random().toArgb()
    }

    /** Return a message representing the total time spent in this activity */
    fun timeSpentLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to track time spent"
        }
        if (totalTimeSpent == 0L) {
            return "Tracking time..."
        }
        return "Total time spent:\n${TimeLabels.convertSecondsToLabel(totalSeconds = totalTimeSpent)}"
    }

    /** Return a message abut the status of the last clock-in  */
    fun lastClockInLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to get started!"
        }
        return "Last clock in:\n${TimeLabels.dateTimeToLabel(dateTime = lastClockIn)}"
    }

    /** Copies and returns given activity, after adjusting some clock in fields */
    fun clockIn(): Activity {

        val now = LocalDateTime.now()
        if (dailyTimes.isEmpty() || dailyTimeDifferentDate(dateTime = now)) {
            addDailyTime(dateTime = now)
        }

        return copy(
            lastClockIn = now,
            isClockedIn = true
        )
    }

    /** Copies and returns given activity, after adjusting some clock out fields */
    fun clockOut(): Activity {

        val now = LocalDateTime.now()
        val totalTimeSpent = calculateTotalTimeSpent(dateTime = now)

        if (dailyTimeDifferentDate(dateTime = now)) {
            determineTimeOverMultipleDays(now = now, totalSeconds = totalTimeSpent)
        } else {
            setDailyTimeSpent(time = calculateDailyTimeSpent(dateTime = now))
        }

        return copy(
            totalTimeSpent = totalTimeSpent,
            isClockedIn = false
        )
    }

    /** Calculate seconds between lastClockIn and given time, and add to running total */
    private fun calculateTotalTimeSpent(dateTime: LocalDateTime): Long =
        Duration.between(lastClockIn, dateTime).seconds + totalTimeSpent

    /** Calculate seconds between lastClockIn and given time,
     *  and add to running total for current day
     * */
    private fun calculateDailyTimeSpent(dateTime: LocalDateTime): Long =
        Duration.between(lastClockIn, dateTime).seconds + dailyTimes.last().timeSpent

    /** Given a date, add a new DailyTime to the end of the list */
    private fun addDailyTime(dateTime: LocalDateTime) {
        dailyTimes.add(DailyTime(date = dateTime))
    }

    /** Determine if latest DailyTime is the current date's, or another day's */
    private fun dailyTimeDifferentDate(dateTime: LocalDateTime): Boolean =
        dailyTimes.last().date?.toLocalDate() != dateTime.toLocalDate()

    /** Input time in seconds to current DailyTime */
    private fun setDailyTimeSpent(time: Long) {
        dailyTimes[dailyTimes.lastIndex] = dailyTimes.last().copy(timeSpent = time)
    }

    /** If user has been clocked in over multiple days,
     *  determine how many seconds to add to each day
     * */
    private fun determineTimeOverMultipleDays(now: LocalDateTime, totalSeconds: Long) {

        val lastClockInToMidnightThatDay = fromTimeToMidnight(time = lastClockIn).seconds
        setDailyTimeSpent(time = lastClockInToMidnightThatDay)

        // Now, add new days
        var remainderSeconds = totalSeconds - lastClockInToMidnightThatDay
        val totalDays = remainderSeconds / TimeLabels.SECONDS_IN_DAY

        addDays(totalDays = totalDays)
        remainderSeconds -= TimeLabels.SECONDS_IN_DAY * totalDays
        addFinalDay(now = now, secondsToAdd = remainderSeconds)
    }

    /** Return a Duration from the given time up to midnight that (same) day
     * -- Duration format: hard to reason with --
     * */
    private fun fromTimeToMidnight(time: LocalDateTime?): Duration = Duration.between(
        time, time?.toLocalDate()?.plusDays(1)?.atStartOfDay()
    )

    /** Add all seconds in a day, per day given */
    private fun addDays(totalDays: Long) {
        if (lastClockIn != null) {
            for (day in 1..totalDays) {
                addDailyTime(dateTime = lastClockIn.plusDays(day))
                setDailyTimeSpent(time = TimeLabels.SECONDS_IN_DAY)
            }
        }
    }

    /** Add remaining seconds to current day */
    private fun addFinalDay(now: LocalDateTime ,secondsToAdd: Long) {
        if (secondsToAdd > 0) {
            addDailyTime(dateTime = now)
            setDailyTimeSpent(time = secondsToAdd)
        }
    }
}

class InvalidActivityException(message: String): Exception(message)
