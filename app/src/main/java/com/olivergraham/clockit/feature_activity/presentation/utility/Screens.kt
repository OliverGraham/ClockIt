package com.olivergraham.clockit.feature_activity.presentation.utility

sealed class Screen(val route: String) {
    object ActivityScreen: Screen(route = "activity_screen")
    object AddEditActivityScreen: Screen(route = "add_edit_activity_screen")
}
