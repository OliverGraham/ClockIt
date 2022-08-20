package com.olivergraham.clockit.feature_activity.domain.use_case

data class ActivityUseCases(
    val addActivity: AddActivity,
    val deleteActivity: DeleteActivity,
    val getActivities: GetActivities,
    val getActivity: GetActivity,
    val updateActivity: UpdateActivity
)
