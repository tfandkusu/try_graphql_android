package com.tfandkusu.graphql.viewmodel.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfandkusu.graphql.usecase.home.EditOnCreateUseCase
import com.tfandkusu.graphql.viewmodel.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditViewModelImpl @Inject constructor(
    private val onCreateUseCase: EditOnCreateUseCase
) : EditViewModel, ViewModel() {
    override fun createDefaultState() = EditState()

    private val _state = MutableLiveData(EditState())

    override val state: LiveData<EditState>
        get() = _state

    private val effectChannel = createEffectChannel()

    override val effect: Flow<EditEffect> = effectChannel.receiveAsFlow()

    override fun event(event: EditEvent) {
        viewModelScope.launch {
            when (event) {
                is EditEvent.OnCreate -> {
                    val issue = onCreateUseCase.execute(event.number)
                    issue?.let {
                        _state.update {
                            copy(
                                progress = false,
                                number = it.number,
                                title = it.title,
                                closed = it.closed,
                                submitEnabled = it.title.isNotEmpty()
                            )
                        }
                    }
                }
                is EditEvent.Submit -> {
                }
                is EditEvent.UpdateTitle -> {
                }
            }
        }
    }
}
