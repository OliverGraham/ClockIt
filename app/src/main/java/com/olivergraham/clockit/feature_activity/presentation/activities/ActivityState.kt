package com.olivergraham.clockit.feature_activity.presentation.activities

import com.olivergraham.clockit.feature_activity.domain.model.Activity

data class ActivityState(
    val activities: List<Activity> = emptyList(),
    // val activityOrder: ActivityOrder = ActivityOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val clockedInActivityId: Int? = null,
    // val clockedInActivityId: Int? = null, was val, checking performance...
    // val maxBarValue: Float = 0F,
    // var barChartSummaryMap: MutableMap<Int?, BarChartState> = mutableMapOf()
)

/*
data class BarChartState(
    val timeModifier: Float = 1F,
    val maxBarValue: Float = 60F,
    val timeUnitLabel: String = "Seconds"
)*/
