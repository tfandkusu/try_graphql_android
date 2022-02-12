package com.tfandkusu.graphql.usecase.edit

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject

data class EditLoadUseCaseResult(
    val editMode: Boolean,
    val issue: GithubIssue?
)

interface EditLoadUseCase {
    suspend fun execute(number: Int): EditLoadUseCaseResult
}

class EditLoadUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : EditLoadUseCase {
    override suspend fun execute(number: Int): EditLoadUseCaseResult {
        return if (number == 0) {
            // Add Issue
            EditLoadUseCaseResult(
                false,
                GithubIssue(
                    "",
                    0,
                    "",
                    false
                )
            )
        } else {
            // Edit Issue
            EditLoadUseCaseResult(
                true,
                repository.get(number)
            )
        }
    }
}
