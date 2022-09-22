package com.olivergraham.clockit.feature_activity.presentation.activities

import com.olivergraham.clockit.feature_activity.domain.model.Activity

data class ActivityState(
    val activities: List<Activity> = emptyList(),
    // val activityOrder: ActivityOrder = ActivityOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val clockedInActivityId: Int? = null,
    val maxBarValue: Float = 0F
)
