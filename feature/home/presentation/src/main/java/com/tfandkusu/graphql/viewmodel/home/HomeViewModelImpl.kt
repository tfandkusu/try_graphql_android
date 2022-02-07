package com.tfandkusu.graphql.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfandkusu.graphql.usecase.home.HomeLoadUseCase
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.usecase.home.HomeReloadUseCase
import com.tfandkusu.graphql.viewmodel.error.ApiErrorShowKind
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper
import com.tfandkusu.graphql.viewmodel.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val onCreateUseCase: HomeOnCreateUseCase,
    private val loadUseCase: HomeLoadUseCase,
    private val reloadUseCase: HomeReloadUseCase
) : HomeViewModel, ViewModel() {

    override fun createDefaultState() = HomeState()

    private val _state = MutableLiveData(createDefaultState())

    override val state: LiveData<HomeState> = _state

    private val effectChannel = createEffectChannel()

    override val effect: Flow<HomeEffect> = effectChannel.receiveAsFlow()

    override val error = ApiErrorViewModelHelper()

    private var loaded = false

    override fun event(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                HomeEvent.OnCreate -> {
                    if (loaded)
                        return@launch
                    loaded = true
                    onCreateUseCase.execute().collect { issues ->
                        _state.update {
                            copy(issues = issues, progress = false)
                        }
                    }
                }
                HomeEvent.Load -> {
                    error.release()
                    try {
                        loadUseCase.execute()
                    } catch (e: Throwable) {
                        error.catch(e, showKind = ApiErrorShowKind.SCREEN)
                    }
                }
                HomeEvent.Reload -> {
                    try {
                        _state.update {
                            copy(refresh = true)
                        }
                        reloadUseCase.execute()
                    } catch (e: Throwable) {
                        error.catch(e, showKind = ApiErrorShowKind.SCREEN)
                    } finally {
                        _state.update {
                            copy(refresh = false)
                        }
                    }
                }
            }
        }
    }
}
