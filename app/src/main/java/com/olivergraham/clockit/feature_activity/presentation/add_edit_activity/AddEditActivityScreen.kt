package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.components.TransparentHintTextField
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityScreen(
    navController: NavController,
    activityColor: Int,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    val titleState = viewModel.activityTitle.value
    val activityBackgroundAnimatable = remember {
        Animatable(
            Color(if (activityColor != -1) activityColor else viewModel.activityColor.value)
        )
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    CollectEvents(viewModel.eventFlow, snackBarHostState, navController)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditActivityEvent.SaveActivity)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save activity")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(activityBackgroundAnimatable.value)
            .padding(16.dp)


        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Activity.activityColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.activityColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    activityBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditActivityEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditActivityEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditActivityEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun CollectEvents(
    eventFlow: SharedFlow<AddEditActivityViewModel.UiEvent>,
    snackBarState: SnackbarHostState,
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is AddEditActivityViewModel.UiEvent.ShowSnackbar -> {
                    snackBarState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditActivityViewModel.UiEvent.SaveActivity -> {
                    navController.navigateUp()
                }
            }
        }
    }
}