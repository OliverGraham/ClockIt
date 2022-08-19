package com.olivergraham.clockit.feature_activity.presentation.activities

// import androidx.compose.material3.rememberScaffoldState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
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

    ) { padding ->
        ActivitiesViewPager(padding, state.activities)
    }
}

/**
 * This is the dipping-style pager
 * */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ActivitiesViewPager(padding: PaddingValues, activities: List<Activity>) {

    // PaddingValues(end = 64.dp)) will show the next page's number
    HorizontalPager(count = activities.size, contentPadding = padding) { page ->
        Box(
            Modifier.graphicsLayer { ->
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

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
            val name = remember { activities[page].name }
            val color = remember { activities[page].color }

            Text("The saved activity is $name", modifier = Modifier.fillMaxSize().background(color = Color(color)))

        }
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
        start = 0.85f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}

/** Animate alpha between 50% and 100% */
private fun animateAlpha(pageOffset: Float): Dp {
    return lerp(
        start = 0.5f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}
