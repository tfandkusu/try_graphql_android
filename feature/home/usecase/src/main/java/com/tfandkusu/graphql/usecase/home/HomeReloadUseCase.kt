package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import javax.inject.Inject

interface HomeReloadUseCase {
    suspend fun execute()
}

class HomeReloadUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : HomeReloadUseCase {
    override suspend fun execute() {
        repository.reload()
    }
}
