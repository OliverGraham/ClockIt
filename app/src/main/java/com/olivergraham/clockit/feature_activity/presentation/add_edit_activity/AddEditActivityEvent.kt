package com.olivergraham.clockit.feature_activity.presentation.add_edit_activity

import androidx.compose.ui.focus.FocusState

sealed class AddEditActivityEvent{
    data class EnteredTitle(val value: String): AddEditActivityEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditActivityEvent()
    /*data class EnteredContent(val value: String): AddEditActivityEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditActivityEvent()*/
    data class ChangeColor(val color: Int): AddEditActivityEvent()
    object SaveActivity: AddEditActivityEvent()
}