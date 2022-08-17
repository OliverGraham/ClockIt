package com.olivergraham.clockit.feature_activity.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ActivityEntity::class], version = 1)
abstract class ActivityDatabase: RoomDatabase() {

    abstract val dao: ActivityDao
}