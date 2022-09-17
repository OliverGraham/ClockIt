package com.olivergraham.clockit.feature_activity.data

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun ActivityEntity.toActivity(): Activity {
    return Activity(
        name = name,
        color = color,
        isClockedIn = isClockedIn,
        lastClockIn = lastClockIn.toLocalDateTime(),
        timeSpent = timeSpent,
        dailyTimes = dailyTimes,
        id = id
    )
}

fun Activity.toActivityEntity(): ActivityEntity {
    return ActivityEntity(
        name = name,
        color = color,
        isClockedIn = isClockedIn,
        lastClockIn = lastClockIn?.toString() ?: "",
        timeSpent = timeSpent,
        dailyTimes = dailyTimes,
        id = id
    )
}

private fun String.toLocalDateTime(): LocalDateTime? =
    if (this.isEmpty()) null else LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
