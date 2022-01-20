package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HomeReloadUseCaseTest {

    private lateinit var useCase: HomeReloadUseCase

    @MockK(relaxed = true)
    private lateinit var repository: GithubIssueRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = HomeReloadUseCaseImpl(repository)
    }

    @Test
    fun execute() = runBlocking {
        useCase.execute()
        coVerifySequence {
            repository.fetch(true)
        }
    }
}
