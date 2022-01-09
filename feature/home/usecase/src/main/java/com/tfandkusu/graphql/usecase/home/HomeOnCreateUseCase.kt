package com.tfandkusu.graphql.usecase.home

import com.tfandkusu.graphql.data.repository.GithubRepoRepository
import com.tfandkusu.graphql.model.GithubRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface HomeOnCreateUseCase {
    fun execute(): Flow<List<GithubRepo>>
}

class HomeOnCreateUseCaseImpl @Inject constructor(
    private val repository: GithubRepoRepository
) : HomeOnCreateUseCase {
    override fun execute() = repository.listAsFlow()
}
