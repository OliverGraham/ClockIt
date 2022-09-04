package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.presentation.common_components.Fab
import com.olivergraham.clockit.feature_activity.presentation.common_components.LargeButton
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
import com.olivergraham.clockit.feature_activity.presentation.utility.navigateWithActivity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navController: NavController,
    activityViewModel: ActivityViewModel = hiltViewModel()
) {
    val state = activityViewModel.state.value
    val activities = state.activities

    // determine if an activity is already clocked in
    /*val clockedInActivityId = remember { mutableStateOf(value = -1) }
    for (activity in activities) {
        if (activity.isClockedIn) {
            clockedInActivityId.value = activity.id!!
            break
        }
    }*/

    val clockedInActivity by remember(activities) {
        derivedStateOf { activities.firstOrNull { activity -> activity.isClockedIn } }
    }

    val snackBarHostState = remember { SnackbarHostState() }

    ObserveUiEvents(activityViewModel.eventFlow, snackBarHostState)

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
                }
            )
        }
    }
}

@Composable
private fun ObserveUiEvents(
    eventFlow: SharedFlow<ActivityViewModel.UiEvent>,
    snackBarState: SnackbarHostState
) {
    LaunchedEffect(key1 = true) { ->
        eventFlow.collectLatest { event ->
            when (event) {
                is ActivityViewModel.UiEvent.ClockIn -> {
                   /* snackBarState.showSnackbar(
                        message = event.message
                    )*/
                }
                is ActivityViewModel.UiEvent.ShowSnackbar -> {
                    snackBarState.showSnackbar(
                        message = event.message
                    )
                }
                else -> {}
            }
        }
    }
}


@Composable
private fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Clock it") },
        actions = { ->
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Reorder,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}


/**
 * This is the dipping-style pager
 * */
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ActivitiesViewPager(
    padding: PaddingValues,
    activities: List<Activity>,
    clockedInActivityId: Int?,
    clockIn: (activity: Activity) -> Unit,
    clockOut: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {


    // PaddingValues(end = 64.dp)) will show the next page's number
    HorizontalPager(
        count = activities.size,
        contentPadding = PaddingValues(start = 64.dp, end = 64.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),

    ) { pageNumber ->
        ElevatedCard(
            Modifier.graphicsLayer { ->
                val pageOffset = calculateCurrentOffsetForPage(pageNumber).absoluteValue

                // (remove the .also blocks to straighten the animation)
                animateXAndY(pageOffset).also { scale ->
                    scaleX = scale.value
                    scaleY = scale.value
                }
                alpha = animateAlpha(pageOffset).also { scale ->
                    scaleX = scale.value
                    scaleY = scale.value
                }.value
            }
        ) { ->
            ActivityCardContent(
                activity = activities[pageNumber],
                clockedInActivityId = clockedInActivityId,
                clockIn = clockIn,
                clockOut = clockOut,
                navigateWithActivity = navigateWithActivity
            )
        }
    }
}

@Composable
private fun ActivityCardContent(
    activity: Activity,
    clockedInActivityId: Int?,
    clockIn: (activity: Activity) -> Unit,
    clockOut: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
            .background(color = Color(activity.color))
    ) { ->

        CardHeader(
            activity = activity,
            onDelete = {},
            navigateWithActivity = navigateWithActivity
        )

        Text(
            text = "Last clock in:\n${activity.mostRecentClockInAsLabel()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Total time spent:\n${activity.timeSpentAsLabel()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Column { ->
            LargeButton(
                text = "Clock In",
                enabled = clockedInActivityId == null && !activity.isClockedIn,
                onClick = {
                    clockIn(activity)

                }
            )
            Spacer(modifier = Modifier.padding(6.dp))
            LargeButton(
                text = "Clock Out",
                enabled = clockedInActivityId == activity.id || activity.isClockedIn,
                onClick = {
                    clockOut(activity)
                }
            )

        }
        Column { ->
            LargeButton(text = "Delete", onClick = {
                /* TODO: delete and show an undo in SnackBar */
                }
            )
        }

    }
}

@Composable
private fun CardHeader(
    activity: Activity,
    onDelete: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit
) {
    val expanded = remember { mutableStateOf(value = false) }
    Row(
        horizontalArrangement = Arrangement.SpaceAround
    ) { ->
        Text(
            text = activity.name,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { expanded.value = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options button")
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false}
        ) { ->
            DropdownMenuItem(
                text = { Text(text = "Edit Activity") },
                onClick = { navigateWithActivity(activity) }
            )
            DropdownMenuItem(
                text = { Text(text = "Delete Activity") },
                onClick = { /*TODO*/ }
            )
        }
    }
}


/** Necessary animation for paging? */
private fun animateXAndY(pageOffset: Float): Dp {
    return lerp(
        start = 0.95f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}

/** Animate alpha */
private fun animateAlpha(pageOffset: Float): Dp {
    return lerp(
        start = 0.95f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}
