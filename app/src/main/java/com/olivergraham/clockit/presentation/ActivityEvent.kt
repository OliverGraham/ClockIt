package com.olivergraham.clockit.presentation

sealed class ActivityEvent {
    object ClockIn: ActivityEvent()
    object ClockOut: ActivityEvent()
    data class OnSomeButtonTap(val someInfo: String): ActivityEvent()
}
