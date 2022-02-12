package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EditSubmitUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var repository: GithubIssueRepository

    private lateinit var useCase: EditSubmitUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = EditSubmitUseCaseImpl(repository)
    }

    @Test
    fun update() = runBlocking {
        val issue = GitHubIssueCatalog.getList().last()
        useCase.execute(issue)
        coVerifySequence {
            repository.update(issue)
        }
    }

    @Test
    fun create() = runBlocking {
        val issue = GithubIssue("", 0, "Create Issue", false)
        useCase.execute(issue)
        coVerifySequence {
            repository.create(issue)
        }
    }
}
