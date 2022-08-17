package com.olivergraham.clockit.domain.repository

import com.olivergraham.clockit.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {

    suspend fun addNewActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    fun getAllActivities(): Flow<List<Activity>>
}