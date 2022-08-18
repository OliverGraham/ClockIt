package com.olivergraham.clockit.feature_activity.presentation.utility

sealed class Screen(val route: String) {
    object ActivityScreen: Screen("activity_screen")
    object AddEditActivityScreen: Screen("add_edit_activity_screen")
}
