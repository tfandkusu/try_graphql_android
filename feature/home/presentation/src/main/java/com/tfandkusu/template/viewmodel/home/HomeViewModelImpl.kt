package com.tfandkusu.template.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfandkusu.template.usecase.home.HomeLoadUseCase
import com.tfandkusu.template.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.template.viewmodel.error.ApiErrorViewModelHelper
import com.tfandkusu.template.viewmodel.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val loadUseCase: HomeLoadUseCase,
    private val onCreateUseCase: HomeOnCreateUseCase
) : HomeViewModel, ViewModel() {

    private val _error = ApiErrorViewModelHelper()

    override val error: ApiErrorViewModelHelper
        get() = _error

    override fun createDefaultState() = HomeState()

    private val _state = MutableLiveData(createDefaultState())

    override val state: LiveData<HomeState> = _state

    private val effectChannel = createEffectChannel()

    override val effect: Flow<HomeEffect> = effectChannel.receiveAsFlow()

    override fun event(event: HomeEvent) {
        viewModelScope.launch {
            if (event is HomeEvent.OnCreate) {
                onCreateUseCase.execute().collect { repos ->
                    _state.update {
                        copy(repos = repos)
                    }
                }
            } else if (event is HomeEvent.Load) {
                error.release()
                _state.update {
                    copy(progress = true)
                }
                try {
                    loadUseCase.execute()
                } catch (e: Throwable) {
                    _error.catch(e)
                } finally {
                    _state.update {
                        copy(progress = false)
                    }
                }
            }
        }
    }
}
