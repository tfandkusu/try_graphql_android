package com.tfandkusu.graphql.view.error

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfandkusu.graphql.viewcommon.R
import com.tfandkusu.graphql.viewmodel.error.ApiErrorState
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper

@Composable
fun ApiErrorOnDialog(
    apiErrorViewModelHelper: ApiErrorViewModelHelper,
    state: ApiErrorState
) {
    if (state.hasErrorOnDialog()) {
        AlertDialog(
            onDismissRequest = {
                apiErrorViewModelHelper.release()
            },
            title = {
                Text(text = stringResource(R.string.title_error))
            },
            text = {
                Text(makeApiErrorMessage(state))
            },
            confirmButton = {
                TextButton(
                    onClick = { // confirmをタップしたとき
                        apiErrorViewModelHelper.release()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}
