package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.presentation.activities.components.ActivityFab
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.AddEditActivityViewModel
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
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
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            ActivityFab(
                onClick = { navController.navigate(Screen.AddEditActivityScreen.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = { TopAppBar() }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize()) { ->
            ActivitiesViewPager(
                padding = padding,
                activities =  state.activities,
                clockIn = { activity ->
                    activityViewModel.onEvent(ActivityEvent.ClockIn(activity = activity))
                },
                clockOut = { activity ->
                    activityViewModel.onEvent(ActivityEvent.ClockOut(activity = activity))
                }

            )
        }
    }
}

@Composable
private fun ObserveUiEvents(
    eventFlow: SharedFlow<ActivityViewModel.UiEvent>,
    // snackBarState: SnackbarHostState,
    navController: NavController
) {
    LaunchedEffect(key1 = true) { ->
        eventFlow.collectLatest { event ->
            when (event) {
                is ActivityViewModel.UiEvent.ClockIn -> {
                   /* snackBarState.showSnackbar(
                        message = event.message
                    )*/
                }
            }
        }
    }
}


@Composable
private fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text("Clock it") },
        actions = {
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
    clockIn: (activity: Activity) -> Unit,
    clockOut: (activity: Activity) -> Unit
) {

    // PaddingValues(end = 64.dp)) will show the next page's number
    HorizontalPager(
        count = activities.size,
        contentPadding = PaddingValues(start = 64.dp, end = 64.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight() //and this
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
            val currentActivity = activities[pageNumber]
            val name = currentActivity.name
            val color = currentActivity.color
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.fillMaxSize()
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .background(color = Color(color))
            ) { ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = currentActivity.mostRecentClockIn,
                    style = MaterialTheme.typography.headlineSmall
                )

                Column { ->
                    LargeButton(text = "Clock In", onClick = {
                        clockIn(currentActivity)
                    })
                    Spacer(modifier = Modifier.padding(6.dp))
                    LargeButton(text = "Clock Out", onClick = { clockOut(currentActivity) })

                }
                Column{ ->
                    LargeButton(text = "Delete", onClick = { /*TODO*/ })
                }

            }


        }
    }
}

@Composable
private fun LargeButton(
    text: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = Modifier
            .size(width = 180.dp, height = 45.dp),
        onClick = { onClick() }
    ) { ->
        Text(text = text)
    }
}

@Composable
private fun ActivityPageContent() {
    // backgroundColor

    // Name of Activity
    // Icon??
    // Clock In

    // Clock Out
    // Time spent
    // Calendar?
}


/** Start at 85% below top of screen and animate to 100% */
private fun animateXAndY(pageOffset: Float): Dp {
    return lerp(
        start = 0.95f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}

/** Animate alpha between 50% and 100% */
private fun animateAlpha(pageOffset: Float): Dp {
    return lerp(
        start = 0.95f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}
