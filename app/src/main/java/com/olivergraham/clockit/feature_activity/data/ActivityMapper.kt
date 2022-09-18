package com.olivergraham.clockit.feature_activity.data

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun ActivityEntity.toActivity(): Activity {
    return Activity(
        name = name,
        color = color,
        isClockedIn = isClockedIn,
        lastClockIn = lastClockIn.toLocalDateTime(),
        timeSpent = timeSpent,
        dailyTimes = dailyTimes.convertLocalDateTimes(),
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
        dailyTimes = dailyTimes.toEntities(),
        id = id
    )
}

private fun String.toLocalDateTime(): LocalDateTime? =
    if (this.isEmpty()) null else LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)


private fun MutableList<DailyTime>.toEntities() = this.map { dailyTime ->
    DailyTimeEntity(
        timeSpent = dailyTime.timeSpent,
        date = dailyTime.date.toString()
    )
}.toList()

private fun List<DailyTimeEntity>.convertLocalDateTimes() = this.map { dailyTimeEntity ->
    DailyTime(
        timeSpent = dailyTimeEntity.timeSpent,
        date = dailyTimeEntity.date.toLocalDateTime()
    )
}.toMutableList()