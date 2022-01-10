package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GithubIssueRepository {
    fun listAsFlow(): Flow<List<GithubIssue>>
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore
) : GithubIssueRepository {
    override fun listAsFlow() = remoteDataStore.listAsFlow()
}
