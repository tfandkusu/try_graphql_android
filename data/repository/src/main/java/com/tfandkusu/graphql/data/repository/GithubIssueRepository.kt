package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface GithubIssueRepository {
    fun listAsFlow(): Flow<List<GithubIssue>>

    suspend fun reload()

    suspend fun get(number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore
) : GithubIssueRepository {
    override fun listAsFlow() = remoteDataStore.listAsFlow(false)

    override suspend fun reload() {
        remoteDataStore.listAsFlow(true)
    }

    override suspend fun get(number: Int) = remoteDataStore.get(false, number)

    override suspend fun update(issue: GithubIssue) {
        remoteDataStore.update(issue)
        remoteDataStore.listAsFlow(true).first()
        remoteDataStore.get(true, issue.number)
    }
}
