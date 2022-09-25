package com.olivergraham.clockit.feature_activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime

@Entity(tableName = "activity_table")
data class ActivityEntity(
    val name: String,
    val color: Int,
    val isClockedIn: Boolean,
    val lastClockIn: String,
    val timeSpent: Long,
    val dailyTimes: List<DailyTimeEntity>,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)