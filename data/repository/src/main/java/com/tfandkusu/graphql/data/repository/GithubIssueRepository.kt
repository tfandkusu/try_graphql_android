package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.local.CreatedLocalDataStore
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.util.CurrentTimeGetter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GithubIssueRepository {

    suspend fun fetch(reload: Boolean)

    fun listAsFlow(): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)
}

class GithubIssueRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubIssueRemoteDataStore,
    private val createdLocalDataStore: CreatedLocalDataStore,
    private val currentTimeGetter: CurrentTimeGetter
) : GithubIssueRepository {

    companion object {
        private const val EXPIRE_TIME = 10 * 60 * 1000
    }

    override suspend fun fetch(reload: Boolean) {
        val now = currentTimeGetter.get()
        if (reload || createdLocalDataStore.get(
                LocalCreated.KIND_GITHUB_ISSUE
            ) + EXPIRE_TIME < now
        ) {
            remoteDataStore.fetch()
            createdLocalDataStore.set(LocalCreated.KIND_GITHUB_ISSUE, now)
        }
    }

    override fun listAsFlow() = remoteDataStore.listAsFlow()

    override suspend fun get(number: Int) = remoteDataStore.get(number)

    override suspend fun update(issue: GithubIssue) {
        remoteDataStore.update(issue)
        remoteDataStore.fetch()
    }
}
