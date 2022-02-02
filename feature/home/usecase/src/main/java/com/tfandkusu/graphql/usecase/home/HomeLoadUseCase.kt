package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import javax.inject.Inject

interface HomeLoadUseCase {
    suspend fun execute()
}

class HomeLoadUseCaseImpl @Inject constructor(
    private val repository: GithubIssueRepository
) : HomeLoadUseCase {
    override suspend fun execute() {
        repository.fetch(false)
    }
}
