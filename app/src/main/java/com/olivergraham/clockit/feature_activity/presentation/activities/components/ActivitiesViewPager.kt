package com.olivergraham.clockit.feature_activity.presentation.activities.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import kotlin.math.absoluteValue

/**
 * This is the dipping-style pager
 * */
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesViewPager(
    padding: PaddingValues,
    activities: List<Activity>,
    clockedInActivityId: Int?,
    clockIn: (activity: Activity) -> Unit,
    clockOut: (activity: Activity) -> Unit,
    navigateWithActivity: (activity: Activity) -> Unit,
    deleteActivity: (activity: Activity) -> Unit
) {
    // PaddingValues(end = 64.dp)) will show the next page's number
    HorizontalPager(
        count = activities.size,
        contentPadding = PaddingValues(start = 48.dp, end = 48.dp),
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
                activity =  activities[pageNumber],
                clockedInActivityId = clockedInActivityId,
                clockIn = clockIn,
                clockOut = clockOut,
                navigateWithActivity = navigateWithActivity,
                deleteActivity = deleteActivity
            )
        }
    }
}

/** Necessary animation for paging? */
private fun animateXAndY(pageOffset: Float): Dp {
    return lerp(
        start = 0.85f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}

/** Animate alpha */
private fun animateAlpha(pageOffset: Float): Dp {
    return lerp(
        start = 0.85f.dp,
        stop = 1f.dp,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}
