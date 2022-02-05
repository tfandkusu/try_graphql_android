package com.tfandkusu.graphql.viewmodel.error

import io.kotlintest.shouldBe
import org.junit.Test

class ApiErrorStateTest {
    @Test
    fun hasErrorFalse() {
        ApiErrorState().hasError() shouldBe false
    }

    @Test
    fun hasErrorTrue() {
        ApiErrorState(network = true).hasError() shouldBe true
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            ),
            showKind = ApiErrorShowKind.SCREEN
        ).hasError() shouldBe true
        ApiErrorState(unknown = true).hasError() shouldBe true
    }

    @Test
    fun hasErrorOnScreenFalse() {
        ApiErrorState().hasErrorOnScreen() shouldBe false
        ApiErrorState(
            network = true,
            showKind = ApiErrorShowKind.DIALOG
        ).hasErrorOnScreen() shouldBe false
    }

    @Test
    fun hasErrorOnScreenTrue() {
        ApiErrorState(network = true).hasErrorOnScreen() shouldBe true
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            ),
            showKind = ApiErrorShowKind.SCREEN
        ).hasErrorOnScreen() shouldBe true
        ApiErrorState(unknown = true).hasErrorOnScreen() shouldBe true
    }

    @Test
    fun hasErrorOnDialogFalse() {
        ApiErrorState(
            showKind = ApiErrorShowKind.DIALOG
        ).hasErrorOnDialog() shouldBe false
        ApiErrorState(
            network = true,
            showKind = ApiErrorShowKind.SCREEN
        ).hasErrorOnDialog() shouldBe false
    }

    @Test
    fun hasErrorOnDialogTrue() {
        ApiErrorState(
            network = true,
            showKind = ApiErrorShowKind.DIALOG
        ).hasErrorOnDialog() shouldBe true
        ApiErrorState(
            server = ApiServerError(
                503, "Service Unavailable"
            ),
            showKind = ApiErrorShowKind.DIALOG
        ).hasErrorOnDialog() shouldBe true
        ApiErrorState(
            unknown = true,
            showKind = ApiErrorShowKind.DIALOG
        ).hasErrorOnDialog() shouldBe true
    }
}
