package com.tfandkusu.template.usecase.home

import com.tfandkusu.template.data.repository.GithubRepoRepository
import javax.inject.Inject

interface HomeLoadUseCase {
    suspend fun execute()
}

class HomeLoadUseCaseImpl @Inject constructor(
    private val repository: GithubRepoRepository
) : HomeLoadUseCase {
    override suspend fun execute() {
        if (repository.isCacheExpired()) {
            repository.fetch()
        }
    }
}
