package com.olivergraham.clockit.feature_activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime

@Entity(tableName = "activity_table")
data class ActivityEntity(
    val name: String,
    val color: Int,
    val isClockedIn: Boolean,
    val mostRecentClockIn: String,
    val timeSpent: Long,
    val dailyTimes: List<DailyTime>,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)