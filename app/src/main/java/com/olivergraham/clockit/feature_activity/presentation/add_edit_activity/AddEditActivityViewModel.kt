package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.runtime.State
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _activityTextFieldState = mutableStateOf(ActivityTextFieldState(
        hint = "Enter name of your activity"
    ))
    val activityTextFieldState: State<ActivityTextFieldState> = _activityTextFieldState

    /*var activityTextFieldState by mutableStateOf(ActivityTextFieldState(
        hint = "Enter name of your activity"
    ))
        private set*/

    private val _activityColor = mutableStateOf(Activity.activityColors.random().toArgb())
    val activityColor: State<Int> = _activityColor

    /*var activityColor by mutableStateOf(Activity.activityColors.random().toArgb())
        private set*/

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // private var currentActivityId: Int? = null

    init {
        initializeActivity()
    }

    private fun initializeActivity() {
        savedStateHandle.get<Int>("activityId")?.let { activityId ->
            if (activityId == -1) {
                return
            }
            viewModelScope.launch(Dispatchers.IO) { ->
                activityUseCases.getActivity(activityId)?.also { activity ->
                    setScreenActivity(activity)
                }
            }
        }
    }

    private fun setScreenActivity(activity: Activity) {
        // currentActivityId = activity.id
        activityTextFieldState = activityTextFieldState.copy(
            text = activity.name,
            isHintVisible = false
        )
        activityColor = activity.color
    }

    private fun setActivityTextFieldName(newText: String) {
        activityTextFieldState = activityTextFieldState.copy(
            text = newText
        )
    }

    private fun setActivityTextFieldFocusState(focusState: FocusState) {
        activityTextFieldState = activityTextFieldState.copy(
            isHintVisible = !focusState.isFocused &&
                    activityTextFieldState.text.isBlank()
        )
    }

    private fun manageSaveActivity() {
        viewModelScope.launch {
            try {
                activityUseCases.addActivity(
                    Activity(
                        name = activityTextFieldState.text,
                        //timestamp = System.currentTimeMillis(),
                        color = activityColor,
                        //id = currentActivityId
                    )
                )
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
