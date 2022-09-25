package com.olivergraham.clockit.feature_activity.presentation.activities

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olivergraham.clockit.feature_activity.domain.model.Activity
import com.olivergraham.clockit.feature_activity.domain.use_case.ActivityUseCases
import com.olivergraham.clockit.feature_activity.presentation.utility.TimeLabels
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

    private var recentlyDeletedActivity: Activity? = null

    init {
        getActivities()
    }

    /** */
    private fun getActivities() {
        getActivityJob?.cancel()
        getActivityJob = activityUseCases.getActivities()
            .onEach { activities ->
                _state.value = state.value.copy(
                    activities = activities,
                    clockedInActivityId = getClockedInActivityId(activities),
                )
            }
            .launchIn(viewModelScope)
    }

    /** Return the id of the clocked-in activity, if an activity is clocked in */
    private fun getClockedInActivityId(activities: List<Activity>): Int? =
        activities.firstOrNull { activity -> activity.isClockedIn }?.id

    /** */
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
            is ActivityEvent.DeleteActivity -> {
                performDeleteActivity(event)
            }
            is ActivityEvent.RestoreActivity -> {
                performRestoreActivity()
            }
        }
    }

    /*private fun performNavigateToEditScreen(event: ActivityEvent.NavigateToEditScreen) {
        navController.navigateWithActivity(event.activity)
    }*/

    /** */
    private fun performClockIn(event: ActivityEvent.ClockIn) {
        viewModelScope.launch { ->
            val activity = event.activity.clockIn()
            _eventFlow.emit(
               value = UiEvent.ShowSnackbar(message = "Clocking in: ${activity.name}")
            )
            _state.value = state.value.copy(
                clockedInActivityId = activity.id
            )
            activityUseCases.updateActivity(activity)
        }
    }

    /** */
    private fun performClockOut(event: ActivityEvent.ClockOut) {
        viewModelScope.launch { ->

            val activity = event.activity.clockOut()
            val sessionTime = activity.timeSpent - event.activity.timeSpent
            val clockedOutMessage = "Clocked out: ${activity.name}."
            val timeAddedMessage = "Added ${TimeLabels.convertSecondsToLabel(sessionTime)} to total!"

            _eventFlow.emit(
                UiEvent.ShowSnackbar(message = "$clockedOutMessage $timeAddedMessage")
            )
            _state.value = state.value.copy(
                clockedInActivityId = null
            )
            activityUseCases.updateActivity(activity)
        }
    }

    /** Compares DailyTime's timeSpent to the current max bar value, and returns the greatest  */
    /*private fun getMaxBarValue(activity: Activity): Float {
        val dailyTimeSpent = activity.dailyTimes.last().timeSpent
        val stateMaxValue = _state.value.maxBarValue
        return (if (dailyTimeSpent > stateMaxValue) dailyTimeSpent else stateMaxValue).toFloat()

    }*/

    /** */
    private fun performDeleteActivity(event: ActivityEvent.DeleteActivity) {
        viewModelScope.launch { ->
            val activity = event.activity
            activityUseCases.deleteActivity(activity)
            recentlyDeletedActivity = activity
            _eventFlow.emit(
                UiEvent.ShowSnackbarWithAction(
                    message = "Deleted ${activity.name}!",
                    action = "Undo"
                )
            )
        }
    }

    /** */
    private fun performRestoreActivity() {
        viewModelScope.launch { ->
            activityUseCases.addActivity(activity = recentlyDeletedActivity ?: return@launch)
            recentlyDeletedActivity = null
        }
    }

    /** */
    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class ShowSnackbarWithAction(val message: String, val action: String): UiEvent()
    }
}