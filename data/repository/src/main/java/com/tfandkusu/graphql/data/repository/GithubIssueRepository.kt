package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GithubIssueRepository {

    suspend fun fetch(reload: Boolean)

    fun listAsFlow(): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore
) : GithubIssueRepository {

    override suspend fun fetch(reload: Boolean) {
        remoteDataStore.fetch()
    }

    override fun listAsFlow() = remoteDataStore.listAsFlow()

    override suspend fun get(number: Int) = remoteDataStore.get(false, number)

    override suspend fun update(issue: GithubIssue) {
        remoteDataStore.update(issue)
        remoteDataStore.fetch()
        remoteDataStore.get(true, issue.number)
    }
}
