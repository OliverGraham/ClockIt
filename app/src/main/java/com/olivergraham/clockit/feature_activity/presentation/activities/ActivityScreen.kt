package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.olivergraham.clockit.feature_activity.presentation.activities.components.ActivitiesViewPager
import com.olivergraham.clockit.feature_activity.presentation.activities.components.BarChartView
import com.olivergraham.clockit.feature_activity.presentation.activities.components.NoActivitiesMessage
import com.olivergraham.clockit.feature_activity.presentation.activities.components.TopAppBar
import com.olivergraham.clockit.feature_activity.presentation.common_components.Fab
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
import com.olivergraham.clockit.feature_activity.presentation.utility.navigateWithActivity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    activityViewModel: ActivityViewModel = hiltViewModel()
) {
    val state = activityViewModel.state.value
    val activities = state.activities

    // determine if an activity is already clocked in
    val clockedInActivity by remember(activities) {
        derivedStateOf { activities.firstOrNull { activity -> activity.isClockedIn } }
    }

    val snackBarHostState = remember { SnackbarHostState() }

    ObserveUiEvents(
        eventFlow = activityViewModel.eventFlow,
        snackBarState = snackBarHostState,
        restoreActivity = { activityViewModel.onEvent(ActivityEvent.RestoreActivity) }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) } ,
        floatingActionButton = {
            Fab(
                contentDescription = "Add activity",
                onClick = { navController.navigate(Screen.AddEditActivityScreen.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = { TopAppBar() }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) { ->

            if (activities.isEmpty()) {
                NoActivitiesMessage()
            } else {

                ActivitiesViewPager(
                    padding = padding,
                    activities = activities,
                    clockedInActivityId = clockedInActivity?.id,
                    clockIn = { activity ->
                        activityViewModel.onEvent(ActivityEvent.ClockIn(activity = activity))
                    },
                    clockOut = { activity ->
                        activityViewModel.onEvent(ActivityEvent.ClockOut(activity = activity))
                    },
                    navigateWithActivity = { activity ->
                        navController.navigateWithActivity(activity = activity)
                    },
                    deleteActivity = { activity ->
                        activityViewModel.onEvent(ActivityEvent.DeleteActivity(activity = activity))
                    }
                )
            }
        }
    }
}


@Composable
private fun ObserveUiEvents(
    eventFlow: SharedFlow<ActivityViewModel.UiEvent>,
    snackBarState: SnackbarHostState,
    restoreActivity: (ActivityEvent.RestoreActivity) -> Unit
) {
    LaunchedEffect(key1 = true) { ->
        eventFlow.collectLatest { event ->
            when (event) {
                is ActivityViewModel.UiEvent.ShowSnackbar -> {
                    snackBarState.showSnackbar(
                        message = event.message
                    )
                }
                is ActivityViewModel.UiEvent.ShowSnackbarWithAction -> {
                    val result = snackBarState.showSnackbar(
                        message = event.message,
                        actionLabel = "Undo"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        restoreActivity(ActivityEvent.RestoreActivity)
                    }
                }
            }
        }
    }
}
