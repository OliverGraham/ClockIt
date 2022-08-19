package com.olivergraham.clockit.feature_activity.presentation.activities

import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.utility.getCurrentDateTime
import java.util.Date

data class ActivityState(
    val activities: List<Activity> = emptyList(),
   // val activityOrder: ActivityOrder = ActivityOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
