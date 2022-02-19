package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EditDeleteUseCaseTest {

    private lateinit var useCase: EditDeleteUseCase

    @MockK(relaxed = true)
    private lateinit var repository: GithubIssueRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = EditDeleteUseCaseImpl(repository)
    }

    @Test
    fun execute() = runBlocking {
        useCase.execute("id_1")
        coVerifySequence {
            repository.delete("id_1")
        }
    }
}
