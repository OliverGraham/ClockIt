package com.olivergraham.clockit.feature_activity.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.olivergraham.clockit.feature_activity.domain.model.DailyTime

class Converters {

    @TypeConverter
    fun dailyTimesToJson(dailyTimes: List<DailyTime>): String = Gson().toJson(dailyTimes)

    @TypeConverter
    fun jsonToDailyTimes(json: String) =
        Gson().fromJson(json, Array<DailyTime>::class.java).toList()
}