package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ActivityScreen(
    navController: NavController,
    activityViewModel: ActivityViewModel = hiltViewModel()
) {
}