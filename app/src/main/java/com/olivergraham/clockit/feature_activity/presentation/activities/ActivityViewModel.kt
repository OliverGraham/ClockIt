package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olivergraham.clockit.feature_activity.domain.use_case.ActivityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases
): ViewModel() {

    private val _state = mutableStateOf(ActivityState())
    val state: State<ActivityState> = _state

    private var getActivityJob: Job? = null

    init {
        getActivities()
    }

    fun onEvent(event: ActivityEvent) {
        when (event) {
            is ActivityEvent.ClockIn -> {

            }
            else -> {}
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
    // or just this?
    // var state by mutableStateOf(ActivityState())

    // in LazyColumn
    //      onEvent = viewModel::onEvent

    // one-time events

    //private val _uiEvent = Channel<ActivityEvent>()

    // To use Date
    /*val date = getCurrentDateTime()
    val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")*/

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