package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ColorCircleRow(
    scope: CoroutineScope,
    activityBackgroundAnimatable: Animatable<Color, AnimationVector4D>,
    chosenColor: State<Int>,
    colorChangeEvent: ((colorInt: Int) -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) { ->
        Activity.activityColors.forEach { color ->
            ColorCircleSelect(
                scope = scope,
                activityBackgroundAnimatable = activityBackgroundAnimatable,
                color = color,
                chosenColor = chosenColor,
                colorChangeEvent = { colorInt ->
                    colorChangeEvent(colorInt)
                }
            )
        }
    }
}

@Composable
private fun ColorCircleSelect(
    scope: CoroutineScope,
    activityBackgroundAnimatable: Animatable<Color, AnimationVector4D>,
    color: Color,
    chosenColor: State<Int>,
    colorChangeEvent: ((colorInt: Int) -> Unit)
) {

    val colorInt = color.toArgb()
    Box(modifier = Modifier
        .size(50.dp)
        .shadow(15.dp, CircleShape)
        .clip(CircleShape)
        .background(color)
        .border(
            width = 3.dp,
            color = if (chosenColor.value == colorInt) {
                Color.Black
            } else Color.Transparent,
            shape = CircleShape
        )
        .clickable {
            scope.launch { ->
                activityBackgroundAnimatable.animateTo(
                    targetValue = Color(colorInt),
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
            colorChangeEvent(colorInt)
        }
    )
}