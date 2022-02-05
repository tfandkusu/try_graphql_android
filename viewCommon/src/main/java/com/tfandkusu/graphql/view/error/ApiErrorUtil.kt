package com.tfandkusu.graphql.view.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfandkusu.graphql.viewcommon.R
import com.tfandkusu.graphql.viewmodel.error.ApiErrorState

@Composable
fun makeApiErrorMessage(state: ApiErrorState): String {
    return when {
        state.network -> {
            stringResource(R.string.error_network)
        }
        state.server != null -> {
            stringResource(
                R.string.error_server_error,
                state.server.code,
                state.server.message
            )
        }
        else -> {
            stringResource(R.string.error_unknown)
        }
    }
}
