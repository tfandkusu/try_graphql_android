package com.tfandkusu.graphql.viewmodel.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.usecase.edit.EditOnCreateUseCase
import com.tfandkusu.graphql.usecase.edit.EditSubmitUseCase
import com.tfandkusu.graphql.viewmodel.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditViewModelImpl @Inject constructor(
    private val onCreateUseCase: EditOnCreateUseCase,
    private val submitUseCase: EditSubmitUseCase
) : EditViewModel, ViewModel() {
    override fun createDefaultState() = EditState()

    private val _state = MutableLiveData(EditState())

    override val state: LiveData<EditState>
        get() = _state

    private val effectChannel = createEffectChannel()

    override val effect: Flow<EditEffect> = effectChannel.receiveAsFlow()

    private var firstOnCreate = true

    override fun event(event: EditEvent) {
        viewModelScope.launch {
            when (event) {
                is EditEvent.OnCreate -> {
                    if (firstOnCreate) {
                        firstOnCreate = false
                        val issue = onCreateUseCase.execute(event.number)
                        issue?.let {
                            _state.update {
                                copy(
                                    progress = false,
                                    id = it.id,
                                    number = it.number,
                                    title = it.title,
                                    closed = it.closed,
                                    submitEnabled = it.title.isNotEmpty()
                                )
                            }
                        }
                    }
                }
                is EditEvent.Submit -> {
                    _state.update {
                        copy(progress = true)
                    }
                    submitUseCase.execute(
                        GithubIssue(
                            event.id,
                            event.number,
                            event.title,
                            event.closed
                        )
                    )
                    effectChannel.send(EditEffect.BackToHome)
                }
                is EditEvent.UpdateTitle -> {
                    _state.update {
                        copy(
                            title = event.title,
                            submitEnabled = event.title.isNotEmpty()
                        )
                    }
                }
                is EditEvent.UpdateClosed -> {
                    _state.update {
                        copy(closed = event.closed)
                    }
                }
            }
        }
    }
}
