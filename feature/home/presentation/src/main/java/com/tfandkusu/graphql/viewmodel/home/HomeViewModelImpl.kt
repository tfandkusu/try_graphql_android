package com.tfandkusu.graphql.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.viewmodel.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val onCreateUseCase: HomeOnCreateUseCase
) : HomeViewModel, ViewModel() {

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
                        copy(issues = repos, progress = false)
                    }
                }
            }
        }
    }
}
