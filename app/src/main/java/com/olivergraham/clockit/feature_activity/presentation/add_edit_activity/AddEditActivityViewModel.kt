package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.model.InvalidActivityException
import com.olivergraham.clockit.feature_activity.domain.use_case.ActivityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _activityTitle = mutableStateOf(ActivityTextFieldState(
        hint = "Enter name of your activity"
    ))
    val activityTitle: State<ActivityTextFieldState> = _activityTitle

    private val _activityColor = mutableStateOf(Activity.activityColors.random().toArgb())
    val activityColor: State<Int> = _activityColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentActivityId: Int? = null

    init {
        initializeActivity(savedStateHandle)
    }

    private fun initializeActivity(savedStateHandle: SavedStateHandle) {
        savedStateHandle.get<Int>("activityId")?.let { activityId ->
            if (activityId != -1) {
                viewModelScope.launch {
                    activityUseCases.getActivity(activityId)?.also { activity ->
                        currentActivityId = activity.id
                        _activityTitle.value = activityTitle.value.copy(
                            text = activity.name,
                            isHintVisible = false
                        )
                        _activityColor.value = activity.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditActivityEvent) {
        when (event) {
            is AddEditActivityEvent.EnteredTitle -> {
                _activityTitle.value = activityTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditActivityEvent.ChangeTitleFocus -> {
                _activityTitle.value = activityTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            activityTitle.value.text.isBlank()
                )
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
                _activityColor.value = event.color
            }
            is AddEditActivityEvent.SaveActivity -> {
                manageSaveActivity()
            }
        }
    }

    private fun manageSaveActivity() {
        viewModelScope.launch {
            try {
                activityUseCases.addActivity(
                    Activity(
                        name = activityTitle.value.text,
                        //timestamp = System.currentTimeMillis(),
                        color = activityColor.value,
                        id = currentActivityId
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

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveActivity: UiEvent()
    }
}



