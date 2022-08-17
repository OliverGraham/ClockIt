package com.olivergraham.clockit.feature_activity.domain.use_case

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository

class DeleteActivity(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity) {
        repository.deleteActivity(activity)
    }
}