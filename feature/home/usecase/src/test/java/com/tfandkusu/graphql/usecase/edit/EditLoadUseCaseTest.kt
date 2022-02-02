package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EditLoadUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var repository: GithubIssueRepository

    private lateinit var useCase: EditLoadUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = EditLoadUseCaseImpl(repository)
    }

    @Test
    fun execute() = runBlocking {
        val issue = GitHubIssueCatalog.getList().last()
        coEvery {
            repository.get(1)
        } returns issue
        useCase.execute(1) shouldBe issue
        coVerifySequence {
            repository.get(1)
        }
    }
}
