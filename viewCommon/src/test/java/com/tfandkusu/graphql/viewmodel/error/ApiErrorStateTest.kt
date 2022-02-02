package com.tfandkusu.graphql.viewmodel.error

import io.kotlintest.shouldBe
import org.junit.Test

class ApiErrorStateTest {

    @Test
    fun hasErrorOnScreenFalse() {
        ApiErrorState().hasErrorOnScreen() shouldBe false
        ApiErrorState(network = true, dialogOrScreen = true).hasErrorOnScreen() shouldBe false
    }

    @Test
    fun hasErrorOnScreenTrue() {
        ApiErrorState(network = true).hasErrorOnScreen() shouldBe true
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            )
        ).hasErrorOnScreen() shouldBe true
        ApiErrorState(unknown = true).hasErrorOnScreen() shouldBe true
    }

    @Test
    fun hasErrorOnDialogFalse() {
        ApiErrorState(dialogOrScreen = true).hasErrorOnDialog() shouldBe false
        ApiErrorState(network = true, dialogOrScreen = false).hasErrorOnDialog() shouldBe false
    }

    @Test
    fun hasErrorOnDialogTrue() {
        ApiErrorState(network = true, dialogOrScreen = true).hasErrorOnDialog() shouldBe true
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            ),
            dialogOrScreen = true
        ).hasErrorOnDialog() shouldBe true
        ApiErrorState(unknown = true, dialogOrScreen = true).hasErrorOnDialog() shouldBe true
    }
}
