package com.olivergraham.clockit.feature_activity.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ActivityEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ActivityDatabase: RoomDatabase() {

    abstract val dao: ActivityDao
}