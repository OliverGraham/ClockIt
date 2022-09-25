package com.olivergraham.clockit.feature_activity.presentation.utility

import com.olivergraham.clockit.feature_activity.domain.model.DailyTime

object TimeLabels {
    const val SECONDS_IN_DAY = 86400L
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

    fun secondsToHighestUnitLabel(time: Long): String {
        return if (time <= SECONDS_IN_MINUTE) {
            "seconds"
        } else if (time <= SECONDS_IN_HOUR) {
            "minutes"
        } else if (time < SECONDS_IN_DAY) {
            "hours"
        } else {
            "days"
        }
    }

    /**
     * Input: DailyTime that will have the most timeSpent,
     *        in order to determine the highest bar in the chart
     * Return values:
     *   first = DailyTimeSpent in terms of desired unit (hours / seconds for example) // timeModifier
     *   second = MaxBarValue
     *   third = label ("seconds", "minutes", etc.)
     * */
    fun barChartYAxis(dailyTime: DailyTime): Triple<Float, Float, String> {
        val rawTime = dailyTime.timeSpent

        if (rawTime <= SECONDS_IN_MINUTE) {
            return secondsTriple(rawTime = rawTime)
        }

        if (rawTime <= SECONDS_IN_HOUR) {
            return minutesTriple(rawTime = rawTime)
        }

        if (rawTime < SECONDS_IN_DAY) {
            return hoursTriple(rawTime = rawTime)
        }

        return daysTriple(rawTime = rawTime)
    }

    /** Return the relevant Triple...
     *  divisor could be changed? 2L means 30 seconds, 4L would be 15 seconds...
     * */
    private fun secondsTriple(rawTime: Long): Triple<Float, Float, String> = Triple(
            first = 1F,
            second = determineMaxBarValue(
                rawTime = rawTime,
                threshold = SECONDS_IN_MINUTE
            ),
            third = "Seconds"
    )

    /** */
    private fun minutesTriple(rawTime: Long): Triple<Float, Float, String> = Triple(
        first = SECONDS_IN_MINUTE.toFloat(),
        second = determineMaxBarValue(
            rawTime = rawTime.toMinutes(),
            threshold = SECONDS_IN_MINUTE
        ),
        third = "Minutes"
    )

    /** */
    private fun hoursTriple(rawTime: Long): Triple<Float, Float, String> {
        val currentHours = rawTime.toHours()
        val yAxisMaxBarValue = if (currentHours > 6L) currentHours else 6L
        return Triple(
            first = SECONDS_IN_HOUR.toFloat(),
            second = determineMaxBarValue(
                rawTime = currentHours,
                threshold = yAxisMaxBarValue
            ),
            third = "Hours"
        )
    }

    /** Will possibly have future use */
    private fun daysTriple(rawTime: Long): Triple<Float, Float, String> = Triple(
        first = SECONDS_IN_DAY.toFloat(),
        second = determineMaxBarValue(
            rawTime = rawTime.toDays(),
            threshold = 24L
        ),
        third = "Days"
    )

    private fun determineMaxBarValue(rawTime: Long, threshold: Long, divisor: Long = 2L): Float {
        val reducedTime = threshold / divisor
        return if (rawTime <= reducedTime) reducedTime.toFloat() else threshold.toFloat()
    }


}