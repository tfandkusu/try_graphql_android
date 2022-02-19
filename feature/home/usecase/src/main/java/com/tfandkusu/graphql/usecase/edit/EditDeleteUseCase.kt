package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository

interface EditDeleteUseCase {
    suspend fun execute(id: String)
}

class EditDeleteUseCaseImpl(
    private val repository: GithubIssueRepository
) : EditDeleteUseCase {
    override suspend fun execute(id: String) {
        repository.delete(id)
    }
}
