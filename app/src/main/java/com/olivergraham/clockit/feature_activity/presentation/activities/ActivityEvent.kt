package com.olivergraham.clockit.feature_activity.presentation.activities

import java.time.LocalDateTime

// Send events from UI to view model
sealed class ActivityEvent {
    object ClockIn: ActivityEvent()
    object ClockOut: ActivityEvent()
    object PopBackStack: ActivityEvent()

    data class Navigate(val route: String): ActivityEvent()
    data class ShowSnackBar(
        val message: String,
        val time: LocalDateTime
    ): ActivityEvent()
    data class OnSomeButtonTap(val someInfo: String): ActivityEvent()
}
