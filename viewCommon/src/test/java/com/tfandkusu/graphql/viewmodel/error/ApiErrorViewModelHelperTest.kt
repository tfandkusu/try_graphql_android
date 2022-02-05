package com.tfandkusu.graphql.viewmodel.error

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.error.NetworkErrorException
import com.tfandkusu.graphql.error.ServerErrorException
import com.tfandkusu.graphql.error.UnknownErrorException
import com.tfandkusu.graphql.viewmodel.mockStateObserver
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
    fun catchNetworkErrorOnScreen() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(NetworkErrorException(), ApiErrorShowKind.SCREEN)
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
        }
    }

    @Test
    fun catchNetworkErrorOnDialog() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(NetworkErrorException(), ApiErrorShowKind.DIALOG)
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.DIALOG
                )
            )
        }
    }

    @Test
    fun catchServerError() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(ServerErrorException(503, "Service Unavailable"), ApiErrorShowKind.SCREEN)
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(
                ApiErrorState(
                    server = ApiServerError(
                        503,
                        "Service Unavailable"
                    ),
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
        }
    }

    @Test
    fun catchUnknownError() {
        val mockStateObserver = helper.state.mockStateObserver()
        helper.catch(UnknownErrorException(), ApiErrorShowKind.SCREEN)
        verifySequence {
            mockStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(
                ApiErrorState(
                    unknown = true,
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
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
