package com.olivergraham.clockit.feature_activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_table")
data class ActivityEntity(
    val name: String,
    val color: Int,
    val isClockedIn: Boolean,
    val mostRecentClockIn: String,
    val timeSpent: Long,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)