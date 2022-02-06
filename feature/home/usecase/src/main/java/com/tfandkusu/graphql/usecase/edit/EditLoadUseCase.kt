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
    override suspend fun execute(number: Int): GithubIssue? {
        return if (number == 0) {
            // Add Issue
            GithubIssue(
                "",
                0,
                "",
                false
            )
        } else {
            // Edit Issue
            repository.get(number)
        }
    }
}
