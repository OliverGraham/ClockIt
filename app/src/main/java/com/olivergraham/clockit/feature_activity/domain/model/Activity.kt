package com.olivergraham.clockit.feature_activity.domain.model

import androidx.compose.ui.graphics.toArgb
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

    private fun getMaxBar(): Pair<Float, Int> {
        val size = dailyTimes.size - 1
        val minHours = SECONDS_IN_HOUR * 6
        var timeUnit = minHours
        var timeUnitFlag = 2        // hours == 2, minutes == 1, seconds == 0
        var highest = 0L

        if (size < 1) {
            return Pair(1000000F, 0)
        }

        // two most recent days
        for (i in size.downTo(to = size - 1)) {

            val currentTime = dailyTimes[i].timeSpent
            if (currentTime > highest) {
                highest = currentTime
                if (highest <= SECONDS_IN_MINUTE) {
                    timeUnit = highest
                    timeUnitFlag = 0

                } else if (highest <= SECONDS_IN_HOUR) {
                    timeUnit = highest.toMinutes()
                    timeUnitFlag = 1
                } else if (highest <= minHours) {
                    timeUnit = minHours.toHours()
                    timeUnitFlag = 2
                } else {
                    timeUnit = highest.toHours()
                    timeUnitFlag = 2
                }
            }
        }

        // default highest number of hours in bar chart summary
        return Pair(timeUnit.toFloat(), timeUnitFlag)
    }

    fun getBarChartYAxisInfo(): Pair<Float, String> {
        val (time, flag) = getMaxBar()
        val label = if (flag == 0) "seconds" else if (flag == 1) "minutes" else "hours"
        return Pair(time, label)
    }

    companion object {

        /** */
        fun List<Activity>.getHighestDailyTimeSpentForMaxBar(): Float {
            var highest = 0L
            this.forEach { activity ->
                activity.dailyTimes.forEach { dailyTime ->
                    val current = dailyTime.timeSpent
                    if (current > highest) {
                        highest = current
                    }
                }
            }
            return highest.toFloat()
        }

        val activityColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
        fun getRandomColor(): Int = activityColors.random().toArgb()

        // Label in this format:    Sep 16, 2022, 8:16:10 PM
        private fun LocalDateTime.toLabel(): String = this.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        )

        private const val SECONDS_IN_DAY = 86400L
        private const val SECONDS_IN_HOUR = 3600L
        private const val SECONDS_IN_MINUTE = 60L

        private fun Long.toDays(): Long = this / SECONDS_IN_DAY
        private fun Long.toHours(): Long = this / SECONDS_IN_HOUR
        private fun Long.toMinutes(): Long = this / SECONDS_IN_MINUTE

        /** Convert the given number of seconds into a relevant message */
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

    /** Return a message representing the total time spent in this activity */
    fun timeSpentLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to track time spent"
        }
        if (timeSpent == 0L) {
            return "Tracking time..."
        }
        return "Total time spent:\n${convertSecondsToLabel(timeSpent)}"
    }

    /** Return a message abut the status of the last clock-in  */
    fun lastClockInLabel(): String {
        if (lastClockIn == null) {
            return "Clock in to get started!"
        }
        return "Last clock in:\n${lastClockIn.toLabel()}"
    }

    /** Copies and returns given activity, after adjusting some clock in fields */
    fun clockIn(): Activity {

        val dateTime = LocalDateTime.now()
        if (dailyTimes.isEmpty() || dailyTimeDifferentDate(dateTime)) {
            addDailyTime(dateTime = dateTime)
        }

        return copy(
            lastClockIn = dateTime,
            isClockedIn = true
        )
    }

    // TODO: Save date of last clock out??
    /** Copies and returns given activity, after adjusting some clock out fields */
    fun clockOut(): Activity {

        val dateTime = LocalDateTime.now()
        val timeSpent = calculateTimeSpent(dateTime)

        if (dailyTimeDifferentDate(dateTime)) {
            determineTimeOverMultipleDays()
        } else {
            setDailyTimeSpent(time = calculateDailyTimeSpent(dateTime))
        }
        // addTimeToYesterday()
        return copy(
            timeSpent = timeSpent,
            isClockedIn = false
        )
    }

    /** Use to test! Currently, only works with one call */
    private fun addTimeToYesterday() {

        val yesterday = LocalDateTime.now().minusDays(1)
        if (yesterday.toLocalDate() == dailyTimes.last().date?.toLocalDate()) return

        addDailyTime(dateTime = yesterday)
        setDailyTimeSpent(time = 45L)
        val first = dailyTimes[0]
        val second = dailyTimes[1]
        dailyTimes[0] = second
        dailyTimes[1] = first
    }

    /** Calculate seconds between lastClockIn and given time, and add to running total */
    private fun calculateTimeSpent(dateTime: LocalDateTime): Long =
        Duration.between(lastClockIn, dateTime).seconds + timeSpent

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
    private fun determineTimeOverMultipleDays() {

        val totalSeconds = Duration.between(lastClockIn, LocalDateTime.now()).seconds
        val lastClockInToMidnightThatDay = fromTimeToMidnight(lastClockIn).seconds
        setDailyTimeSpent(time = lastClockInToMidnightThatDay)

        // Now, add new days
        var remainderSeconds = totalSeconds - lastClockInToMidnightThatDay
        val totalDays = remainderSeconds / SECONDS_IN_DAY

        addDays(totalDays)
        remainderSeconds -= SECONDS_IN_DAY * totalDays
        addFinalDay(remainderSeconds)
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
                addDailyTime(lastClockIn.plusDays(day))
                setDailyTimeSpent(SECONDS_IN_DAY)
            }
        }
    }

    /** Add remaining seconds to current day */
    private fun addFinalDay(remainderSeconds: Long) {
        if (remainderSeconds > 0) {
            addDailyTime(LocalDateTime.now())
            setDailyTimeSpent(remainderSeconds)
        }
    }
}

class InvalidActivityException(message: String): Exception(message)
