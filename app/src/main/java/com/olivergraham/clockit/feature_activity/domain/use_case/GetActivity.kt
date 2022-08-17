package com.olivergraham.clockit.feature_activity.domain.use_case

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository


class GetActivity(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(id: Int): Activity? {
        return repository.getActivityById(id)
    }
}