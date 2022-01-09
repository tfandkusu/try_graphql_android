package com.tfandkusu.graphql.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MutableLiveDataExtTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    data class Count(val count: Int)

    @Test
    fun updateSuccess() {
        val liveData = MutableLiveData(Count(1))
        liveData.update {
            copy(count = 2)
        }
        liveData.value shouldBe Count(2)
    }

    @Test
    fun updateFail() {
        val liveData = MutableLiveData<Count>()
        shouldThrow<IllegalArgumentException> {
            liveData.update {
                copy(count = 2)
            }
        }
    }

    @Test
    fun coUpdateSuccess() = runBlocking {
        val liveData = MutableLiveData(Count(1))
        liveData.coUpdate {
            copy(count = 2)
        }
        liveData.value shouldBe Count(2)
    }

    @Test
    fun coUpdateFail() = runBlocking {
        val liveData = MutableLiveData<Count>()
        shouldThrow<IllegalArgumentException> {
            liveData.coUpdate {
                copy(count = 2)
            }
        }
        Unit
    }
}
