package com.olivergraham.clockit.feature_activity.presentation.activities

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import java.time.LocalDateTime

// Send events from UI to view model
sealed class ActivityEvent {

    data class ClockIn(val activity: Activity): ActivityEvent()
    data class ClockOut(val activity: Activity): ActivityEvent()
    // data class NavigateToEditScreen(val activity: Activity): ActivityEvent()
    data class DeleteActivity(val activity: Activity): ActivityEvent()

    /*
    object PopBackStack: ActivityEvent()
    data class ShowSnackBar(
        val message: String,
        val time: LocalDateTime
    ): ActivityEvent()
    data class OnSomeButtonTap(val someInfo: String): ActivityEvent()*/
}
