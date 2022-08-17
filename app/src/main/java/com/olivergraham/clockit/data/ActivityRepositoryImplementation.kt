package com.olivergraham.clockit.data

import com.olivergraham.clockit.domain.model.Activity
import com.olivergraham.clockit.domain.repository.ActivityRepository
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

    override fun getAllActivities(): Flow<List<Activity>> {
        return dao.getAllActivities().map { activities ->
            activities.map { it.toActivity() }
        }
    }
}