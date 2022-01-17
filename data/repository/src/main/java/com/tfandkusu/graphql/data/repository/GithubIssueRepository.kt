package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface GithubIssueRepository {
    fun listAsFlow(reload: Boolean): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore
) : GithubIssueRepository {
    override fun listAsFlow(reload: Boolean) = remoteDataStore.listAsFlow(reload)

    override suspend fun get(number: Int) = remoteDataStore.get(false, number)

    override suspend fun update(issue: GithubIssue) {
        remoteDataStore.update(issue)
        remoteDataStore.listAsFlow(true).first()
        remoteDataStore.get(true, issue.number)
    }
}
