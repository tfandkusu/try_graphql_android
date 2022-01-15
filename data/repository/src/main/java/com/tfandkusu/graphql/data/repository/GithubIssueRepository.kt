package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GithubIssueRepository {
    fun listAsFlow(reload: Boolean): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore
) : GithubIssueRepository {
    override fun listAsFlow(reload: Boolean) = remoteDataStore.listAsFlow(reload)

    override suspend fun get(number: Int) = remoteDataStore.get(number)
}
