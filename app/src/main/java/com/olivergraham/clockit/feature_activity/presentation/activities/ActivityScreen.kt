package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    activityViewModel: ActivityViewModel = hiltViewModel()
) {

    //val scaffoldState = rememberScaffoldState()
    Text("The Activity Screen")
}