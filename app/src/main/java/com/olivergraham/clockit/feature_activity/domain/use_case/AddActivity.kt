package com.olivergraham.clockit.feature_activity.domain.use_case

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.InvalidActivityException
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository

class AddActivity(
    private val repository: ActivityRepository
) {

    suspend operator fun invoke(activity: Activity) {
        if (activity.name.isBlank()) {
            throw InvalidActivityException("The activity needs a name")
        }
        // if something else is wrong, throw exception

        repository.addNewActivity(activity)
    }
}