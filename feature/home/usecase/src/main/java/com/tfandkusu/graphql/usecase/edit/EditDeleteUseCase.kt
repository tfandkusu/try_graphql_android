package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import javax.inject.Inject

interface EditDeleteUseCase {
    suspend fun execute(id: String)
}

class EditDeleteUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : EditDeleteUseCase {
    override suspend fun execute(id: String) {
        repository.delete(id)
    }
}
