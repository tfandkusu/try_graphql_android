package com.tfandkusu.template.viewmodel.error

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.template.error.NetworkErrorException
import com.tfandkusu.template.error.ServerErrorException
import com.tfandkusu.template.error.UnknownErrorException
import com.tfandkusu.template.viewmodel.mockStateObserver
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ApiErrorViewModelHelperTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var helper: ApiErrorViewModelHelper

    @Before
    fun setUp() {
        helper = ApiErrorViewModelHelper()
    }

    @Test
    fun catchNetworkError() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(NetworkErrorException())
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(ApiErrorState(network = true))
        }
    }

    @Test
    fun catchServerError() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(ServerErrorException(503, "Service Unavailable"))
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(
                ApiErrorState(
                    server = ApiServerError(
                        503,
                        "Service Unavailable"
                    )
                )
            )
        }
    }

    @Test
    fun catchUnknownError() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(UnknownErrorException())
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(ApiErrorState(unknown = true))
        }
    }

    @Test
    fun release() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.release()
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(ApiErrorState())
        }
    }
}
