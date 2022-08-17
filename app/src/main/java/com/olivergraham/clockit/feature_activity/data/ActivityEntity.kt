package com.olivergraham.clockit.feature_activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_table")
data class ActivityEntity(
    val name: String,
    // time
    @PrimaryKey val id: Int? = null
)