package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

interface HomeReloadUseCase {
    suspend fun execute()
}

class HomeReloadUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : HomeReloadUseCase {
    override suspend fun execute() {
        repository.listAsFlow(true).first()
    }
}
