package com.tfandkusu.graphql.viewmodel.home

import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.viewmodel.UnidirectionalViewModel
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper

sealed class HomeEvent {
    object Load : HomeEvent()
    object OnCreate : HomeEvent()
    object Reload : HomeEvent()
}

sealed class HomeEffect
data class HomeState(
    val progress: Boolean = true,
    val refresh: Boolean = false,
    val issues: List<GithubIssue> = listOf()
)

interface HomeViewModel : UnidirectionalViewModel<HomeEvent, HomeEffect, HomeState> {
    val error: ApiErrorViewModelHelper
}
