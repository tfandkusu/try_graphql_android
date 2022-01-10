package com.tfandkusu.graphql.viewmodel.home

import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.model.GithubRepo
import com.tfandkusu.graphql.viewmodel.UnidirectionalViewModel
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper

sealed class HomeEvent {
    object OnCreate : HomeEvent()
}

sealed class HomeEffect
data class HomeState(
    val progress: Boolean = true,
    val issues: List<GithubIssue> = listOf()
)

interface HomeViewModel : UnidirectionalViewModel<HomeEvent, HomeEffect, HomeState>
