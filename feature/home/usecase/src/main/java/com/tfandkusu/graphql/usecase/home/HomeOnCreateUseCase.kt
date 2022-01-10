package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface HomeOnCreateUseCase {
    fun execute(): Flow<List<GithubIssue>>
}

class HomeOnCreateUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : HomeOnCreateUseCase {
    override fun execute() = repository.listAsFlow(false)
}
