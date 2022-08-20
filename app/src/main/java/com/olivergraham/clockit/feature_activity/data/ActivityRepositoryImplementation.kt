package com.olivergraham.clockit.feature_activity.data

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRepositoryImplementation(
    private val dao: ActivityDao
): ActivityRepository {

    override suspend fun addNewActivity(activity: Activity) {
        dao.insertActivity(activity.toActivityEntity())
    }

    override suspend fun deleteActivity(activity: Activity) {
        dao.deleteActivity(activity.toActivityEntity())
    }

    override suspend fun getActivityById(id: Int): Activity? {
        return dao.getActivityById(id)?.toActivity()
    }

    override fun getAllActivities(): Flow<List<Activity>> {
        return dao.getAllActivities().map { activities ->
            activities.map { it.toActivity() }
        }
    }

    override suspend fun updateActivity(activity: Activity) {
        dao.updateActivity(activity.toActivityEntity())
    }
}