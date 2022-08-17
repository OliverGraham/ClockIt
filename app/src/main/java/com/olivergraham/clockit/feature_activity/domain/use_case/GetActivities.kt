package com.olivergraham.clockit.feature_activity.domain.use_case

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class GetActivities(
    private val repository: ActivityRepository
) {

    operator fun invoke(
        // TODO: add sort-by ordering
    ): Flow<List<Activity>> {
        return repository.getAllActivities()
    }
}