package com.tfandkusu.graphql.viewmodel.edit

import com.tfandkusu.graphql.viewmodel.UnidirectionalViewModel

sealed class EditEvent {
    data class OnCreate(val number: Int) : EditEvent()
    data class UpdateTitle(val title: String) : EditEvent()
    data class UpdateClosed(val closed: Boolean) : EditEvent()
    data class Submit(
        val number: Int?,
        val title: String,
        val closed: Boolean
    ) : EditEvent()
}

sealed class EditEffect {
    object BackToHome : EditEffect()
}

data class EditState(
    val progress: Boolean = true,
    val title: String = "",
    val closed: Boolean = false,
    val submitEnabled: Boolean = false
)

interface EditViewModel : UnidirectionalViewModel<EditEvent, EditEffect, EditState>
