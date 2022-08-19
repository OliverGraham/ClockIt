package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.components.ColorCircleRow
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.components.SaveFab
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.components.TransparentHintTextField
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

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
        floatingActionButton = { SaveFab(
            onClick = { viewModel.onEvent(AddEditActivityEvent.SaveActivity) })
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(activityBackgroundAnimatable.value)
            .padding(16.dp)
        ) { ->
            ColorCircleRow(
                scope = scope,
                activityBackgroundAnimatable = activityBackgroundAnimatable,
                chosenColor = viewModel.activityColor,
                colorChangeEvent = { colorInt ->
                    viewModel.onEvent(AddEditActivityEvent.ChangeColor(colorInt))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = { title ->
                    viewModel.onEvent(AddEditActivityEvent.EnteredTitle(title))
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(AddEditActivityEvent.ChangeTitleFocus(focusState))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
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
    LaunchedEffect(key1 = true) { ->
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