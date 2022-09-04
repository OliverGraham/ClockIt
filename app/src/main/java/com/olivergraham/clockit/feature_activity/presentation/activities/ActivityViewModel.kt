package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olivergraham.clockit.feature_activity.common.Dates
import com.olivergraham.clockit.feature_activity.domain.use_case.ActivityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases
): ViewModel() {

    private val _state = mutableStateOf(ActivityState())
    val state: State<ActivityState> = _state

    private var getActivityJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getActivities()
    }

    fun onEvent(event: ActivityEvent) {
        when (event) {
            is ActivityEvent.ClockIn -> {
                performClockIn(event)
            }
            is ActivityEvent.ClockOut -> {
                performClockOut(event)
            }
            /*is ActivityEvent.NavigateToEditScreen -> {
                performNavigateToEditScreen(event)
            }*/
            /*
            is ActivityEvent.DeleteActivity -> {
                performDeleteActivity(event)
            }*/
            else -> {}
        }
    }

    /*private fun performNavigateToEditScreen(event: ActivityEvent.NavigateToEditScreen) {
        navController.navigateWithActivity(event.activity)
    }*/

    private fun performClockIn(event: ActivityEvent.ClockIn) {
        viewModelScope.launch { ->

            val date = Dates.getCurrentTime()

            val activity = event.activity.copy(
                mostRecentClockIn = date.toString(),
                isClockedIn = true
            )

            activityUseCases.updateActivity(activity)

            _state.value = state.value.copy(
                clockedInActivityId = event.activity.id
            )

            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = "Clocked in at ${Dates.dateToString(date)}")
            )
        }
    }

    private fun performClockOut(event: ActivityEvent.ClockOut) {
        viewModelScope.launch { ->

            val activity = event.activity.copy(
                timeSpent = Dates.calculateTimeSpent(
                    event.activity.mostRecentClockIn, event.activity.timeSpent
                ),
                isClockedIn = false
            )
            activityUseCases.updateActivity(activity)

            _state.value = state.value.copy(
                clockedInActivityId = null
            )
            _eventFlow.emit(UiEvent.ShowSnackbar(
                message = "Clocked out at ${Dates.getCurrentTimeAsString()}")
            )
        }
    }

    private fun getActivities() {
        getActivityJob?.cancel()
        getActivityJob = activityUseCases.getActivities()
            .onEach { activities ->
                _state.value = state.value.copy(
                    activities = activities
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class Navigate(val message: String): UiEvent()
        object ClockIn: UiEvent()
    }

    //
    /*fun onEvent(event: TodoListEvent) {
        when(event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Todo deleted",
                        action = "Undo"
                    ))
                }
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }*/
}