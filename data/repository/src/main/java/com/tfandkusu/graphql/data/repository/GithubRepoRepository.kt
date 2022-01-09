package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.data.local.CreatedLocalDataStore
import com.tfandkusu.graphql.data.local.GithubRepoLocalDataStore
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import com.tfandkusu.graphql.data.remote.GithubRemoteDataStore
import com.tfandkusu.graphql.model.GithubRepo
import com.tfandkusu.graphql.util.CurrentTimeGetter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GithubRepoRepository {

    suspend fun isCacheExpired(): Boolean

    suspend fun fetch()

    fun listAsFlow(): Flow<List<GithubRepo>>
}

class GithubRepoRepositoryImpl @Inject constructor(
    private val remoteDataStore: GithubRemoteDataStore,
    private val localDataStore: GithubRepoLocalDataStore,
    private val createdLocalDataStore: CreatedLocalDataStore,
    private val currentTimeGetter: CurrentTimeGetter
) : GithubRepoRepository {

    companion object {
        private const val CACHE_TIME = 60 * 60 * 1000
    }

    override suspend fun isCacheExpired(): Boolean {
        val saveTime = createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO)
        val elapsedTime = currentTimeGetter.get() - saveTime
        return elapsedTime > CACHE_TIME
    }

    override suspend fun fetch() {
        val repos = remoteDataStore.listRepositories()
        localDataStore.save(repos)
    }

    override fun listAsFlow() = localDataStore.listAsFlow()
}
