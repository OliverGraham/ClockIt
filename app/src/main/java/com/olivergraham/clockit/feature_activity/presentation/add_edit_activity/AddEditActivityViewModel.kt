package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.InvalidActivityException
import com.olivergraham.clockit.feature_activity.domain.use_case.ActivityUseCases
import com.olivergraham.clockit.feature_activity.presentation.utility.getActivityId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var activityTextFieldState by mutableStateOf(ActivityTextFieldState(
        hint = "Enter name of your activity"
    ))
        private set

    var activityColor by mutableStateOf(Activity.activityColors.random().toArgb())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentActivityId: Int? = null
    private var currentActivity: Activity? = null

    init {
        initializeActivity()
    }

    /** Populate screen with navigation argument, if there is one */
    private fun initializeActivity() {
        savedStateHandle.getActivityId()?.let { activityId ->
            if (activityId != -1) {
                viewModelScope.launch { ->
                    currentActivity = withContext(Dispatchers.IO) { ->
                        activityUseCases.getActivity(activityId)
                    }
                    currentActivity?.let { setScreenActivity(it) }
                }
            }
        }
    }

    /** Populate activity fields with navigation argument */
    private fun setScreenActivity(activity: Activity) {
        currentActivityId = activity.id
        activityTextFieldState = activityTextFieldState.copy(
            text = activity.name,
            isHintVisible = false
        )
        activityColor = activity.color
    }

    /** Copy relevant state */
    private fun setActivityTextFieldName(newText: String) {
        activityTextFieldState = activityTextFieldState.copy(
            text = newText
        )
    }

    /** Copy relevant state */
    private fun setActivityTextFieldFocusState(focusState: FocusState) {
        activityTextFieldState = activityTextFieldState.copy(
            isHintVisible = !focusState.isFocused &&
                    activityTextFieldState.text.isBlank()
        )
    }

    /** Save or update activity */
    private fun manageSaveActivity() {
        viewModelScope.launch { ->
            try {
                if (currentActivity == null) saveActivity() else updateActivity()
                _eventFlow.emit(UiEvent.SaveActivity)
            } catch (e: InvalidActivityException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save activity"
                    )
                )
            }
        }
    }

    /** Update given activity in database */
    private suspend fun updateActivity() {
        currentActivity?.let { activity ->
            activityUseCases.updateActivity(
                activity.copy(
                    name = activityTextFieldState.text,
                    color = activityColor
                )
            )
        }
    }

    /** Save new activity in database */
    private suspend fun saveActivity() {
        activityUseCases.addActivity(
            Activity(
                name = activityTextFieldState.text,
                color = activityColor,
                id = currentActivityId
            )
        )
    }


    /** Handle user-directed events */
    fun onEvent(event: AddEditActivityEvent) {
        when (event) {
            is AddEditActivityEvent.EnteredTitle -> {
                setActivityTextFieldName(event.value)
            }
            is AddEditActivityEvent.ChangeTitleFocus -> {
                setActivityTextFieldFocusState(event.focusState)
            }
            /*is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }*/
            is AddEditActivityEvent.ChangeColor -> {
                activityColor = event.color
            }
            is AddEditActivityEvent.SaveActivity -> {
                manageSaveActivity()
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveActivity: UiEvent()
    }
}
