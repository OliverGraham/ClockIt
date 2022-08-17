package com.olivergraham.clockit.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.olivergraham.clockit.utilities.getCurrentDateTime
import com.olivergraham.clockit.utilities.toString
import java.time.LocalDateTime

class ActivityViewModel(): ViewModel() {

    var state by mutableStateOf(ActivityState())

    // To use Date
    /*val date = getCurrentDateTime()
    val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")*/
}