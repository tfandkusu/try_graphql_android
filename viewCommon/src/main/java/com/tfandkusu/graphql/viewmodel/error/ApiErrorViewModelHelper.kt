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

data class ApiErrorState(
    val network: Boolean = false,
    val server: ApiServerError? = null,
    val unknown: Boolean = false
) {
    fun noError(): Boolean {
        return !network && server == null && !unknown
    }
}

class ApiErrorViewModelHelper {

    private val _state = MutableLiveData(ApiErrorState())

    val state: LiveData<ApiErrorState> = _state

    fun catch(e: Throwable) {
        when (e) {
            is NetworkErrorException -> {
                _state.update {
                    copy(network = true)
                }
            }
            is ServerErrorException -> {
                _state.update {
                    copy(server = ApiServerError(e.code, e.httpMessage))
                }
            }
            else -> {
                _state.update {
                    copy(unknown = true)
                }
            }
        }
    }

    fun release() {
        _state.value = ApiErrorState()
    }
}
