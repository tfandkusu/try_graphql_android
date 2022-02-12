package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject

interface EditSubmitUseCase {
    suspend fun execute(issue: GithubIssue)
}

class EditSubmitUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : EditSubmitUseCase {
    override suspend fun execute(issue: GithubIssue) {
        if (issue.id.isNotEmpty()) {
            repository.update(issue)
        } else {
            repository.create(issue)
        }
    }
}
