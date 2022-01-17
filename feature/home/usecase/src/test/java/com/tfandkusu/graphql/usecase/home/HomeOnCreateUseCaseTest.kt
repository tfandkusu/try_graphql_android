package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class HomeOnCreateUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var repository: GithubIssueRepository

    private lateinit var useCase: HomeOnCreateUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = HomeOnCreateUseCaseImpl(repository)
    }

    @Test
    fun execute() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        every {
            repository.listAsFlow()
        } returns flow {
            emit(issues)
        }
        useCase.execute().first() shouldBe issues
        verifySequence {
            repository.listAsFlow()
        }
    }
}
