package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject

interface EditLoadUseCase {
    suspend fun execute(number: Int): GithubIssue?
}

class EditLoadUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : EditLoadUseCase {
    override suspend fun execute(number: Int) = repository.get(number)
}
