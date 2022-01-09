package com.tfandkusu.template.viewmodel.error

import io.kotlintest.shouldBe
import org.junit.Test

class ApiErrorStateTest {

    @Test
    fun noErrorFalse() {
        ApiErrorState(network = true).noError() shouldBe false
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            )
        ).noError() shouldBe false
        ApiErrorState(unknown = true).noError() shouldBe false
    }

    @Test
    fun noErrorSuccess() {
        ApiErrorState().noError() shouldBe true
    }
}
