package com.tfandkusu.graphql.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState

@Composable
inline fun <reified STATE, EFFECT, EVENT> useState(
    viewModel: UnidirectionalViewModel<EVENT, EFFECT, STATE>
): STATE {
    return viewModel.state.observeAsState(viewModel.createDefaultState()).value
}
