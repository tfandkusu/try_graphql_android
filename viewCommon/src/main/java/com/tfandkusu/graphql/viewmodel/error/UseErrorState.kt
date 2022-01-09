package com.tfandkusu.graphql.viewmodel.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun useErrorState(apiErrorViewModelHelper: ApiErrorViewModelHelper): ApiErrorState {
    return apiErrorViewModelHelper.state.observeAsState(ApiErrorState()).value
}
