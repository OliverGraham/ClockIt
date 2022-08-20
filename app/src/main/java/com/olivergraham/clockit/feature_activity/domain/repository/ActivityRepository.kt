package com.olivergraham.clockit.feature_activity.domain.repository

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {

    suspend fun addNewActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun getActivityById(id: Int): Activity?

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun updateActivity(activity: Activity)
}

