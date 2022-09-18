package com.olivergraham.clockit.feature_activity.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun dailyTimesToJson(dailyTimes: List<DailyTimeEntity>): String = Gson().toJson(dailyTimes)

    @TypeConverter
    fun jsonToDailyTimes(json: String) =
        Gson().fromJson(json, Array<DailyTimeEntity>::class.java).toList()
}