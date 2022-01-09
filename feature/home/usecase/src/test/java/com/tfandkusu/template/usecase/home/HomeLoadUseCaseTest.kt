package com.tfandkusu.template.usecase.home

import com.tfandkusu.template.data.repository.GithubRepoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HomeLoadUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var repository: GithubRepoRepository

    private lateinit var useCase: HomeLoadUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = HomeLoadUseCaseImpl(repository)
    }

    @Test
    fun fetch() = runBlocking {
        coEvery {
            repository.isCacheExpired()
        } returns true
        useCase.execute()
        coVerifySequence {
            repository.isCacheExpired()
            repository.fetch()
        }
    }

    @Test
    fun useCache() = runBlocking {
        coEvery {
            repository.isCacheExpired()
        } returns false
        useCase.execute()
        coVerifySequence {
            repository.isCacheExpired()
        }
    }
}
