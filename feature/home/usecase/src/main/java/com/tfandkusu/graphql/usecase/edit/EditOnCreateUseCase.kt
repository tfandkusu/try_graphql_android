package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject

interface EditOnCreateUseCase {
    suspend fun execute(number: Int): GithubIssue?
}

class EditOnCreateUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : EditOnCreateUseCase {
    override suspend fun execute(number: Int) = repository.get(number)
}
