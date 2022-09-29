package com.olivergraham.clockit.feature_activity.presentation.utility

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.olivergraham.clockit.feature_activity.domain.model.Activity

private const val ACTIVITY_ID: String = "activityId"
private const val ACTIVITY_COLOR: String = "activityColor"

object Navigation {
    fun getActivityIdKey() = ACTIVITY_ID
    fun getActivityColorKey() = ACTIVITY_COLOR
}

fun NavBackStackEntry.getColor(): Int = this.arguments?.getInt(ACTIVITY_COLOR) ?: -1

fun NavController.navigateWithActivity(activity: Activity) {
    this.navigate(
        route = Screen.AddEditActivityScreen.route +
                "?$ACTIVITY_ID=${activity.id}&$ACTIVITY_COLOR=${activity.color}"
    )
}

fun NavController.navigateToEditScreenRouteTemplate(): String =
    Screen.AddEditActivityScreen.route +
            "?$ACTIVITY_ID={$ACTIVITY_ID}&$ACTIVITY_COLOR={$ACTIVITY_COLOR}"

fun SavedStateHandle.getActivityId(): Int? = this.get<Int>(ACTIVITY_ID)