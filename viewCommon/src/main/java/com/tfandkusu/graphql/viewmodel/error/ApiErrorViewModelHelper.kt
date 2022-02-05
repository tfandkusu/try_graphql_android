package com.tfandkusu.graphql.viewmodel.error

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tfandkusu.graphql.error.NetworkErrorException
import com.tfandkusu.graphql.error.ServerErrorException
import com.tfandkusu.graphql.viewmodel.update

data class ApiServerError(
    val code: Int,
    val message: String
)

enum class ApiErrorShowKind {
    /**
     * API error show on screen.
     */
    SCREEN,

    /**
     * API error show on dialog.
     */
    DIALOG
}

data class ApiErrorState(
    val network: Boolean = false,
    val server: ApiServerError? = null,
    val unknown: Boolean = false,
    val showKind: ApiErrorShowKind = ApiErrorShowKind.SCREEN
) {
    fun hasError(): Boolean {
        return network || server != null || unknown
    }

    fun hasErrorOnScreen(): Boolean {
        return hasError() && showKind == ApiErrorShowKind.SCREEN
    }

    fun hasErrorOnDialog(): Boolean {
        return hasError() && showKind == ApiErrorShowKind.DIALOG
    }
}

class ApiErrorViewModelHelper {

    private val _state = MutableLiveData(ApiErrorState())

    val state: LiveData<ApiErrorState> = _state

    fun catch(e: Throwable, showKind: ApiErrorShowKind) {
        when (e) {
            is NetworkErrorException -> {
                _state.update {
                    copy(network = true, showKind = showKind)
                }
            }
            is ServerErrorException -> {
                _state.update {
                    copy(server = ApiServerError(e.code, e.httpMessage), showKind = showKind)
                }
            }
            else -> {
                _state.update {
                    copy(unknown = true, showKind = showKind)
                }
            }
        }
    }

    fun release() {
        _state.value = ApiErrorState()
    }
}
