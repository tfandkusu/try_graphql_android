package com.tfandkusu.graphql.data.local

import androidx.room.withTransaction
import com.tfandkusu.graphql.data.local.db.AppDatabase
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import com.tfandkusu.graphql.data.local.entity.LocalGithubRepo
import com.tfandkusu.graphql.model.GithubRepo
import com.tfandkusu.graphql.util.CurrentTimeGetter
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GithubRepoLocalDataStore {
    suspend fun save(githubRepos: List<GithubRepo>)

    fun listAsFlow(): Flow<List<GithubRepo>>

    suspend fun clear()
}

class GithubRepoLocalDataStoreImpl @Inject constructor(
    private val db: AppDatabase,
    private val currentTimeGetter: CurrentTimeGetter
) :
    GithubRepoLocalDataStore {

    private val githubRepoDao = db.githubRepoDao()

    private val createdDao = db.createdDao()

    override suspend fun save(githubRepos: List<GithubRepo>) {
        db.withTransaction {
            githubRepoDao.clear()
            githubRepos.map {
                LocalGithubRepo(
                    0L,
                    it.id,
                    it.name,
                    it.description,
                    it.updatedAt.time,
                    it.language,
                    it.htmlUrl,
                    it.fork
                )
            }.map {
                githubRepoDao.insert(it)
            }
            createdDao.insert(
                LocalCreated(
                    createdDao.get(LocalCreated.KIND_GITHUB_REPO)?.id ?: 0L,
                    LocalCreated.KIND_GITHUB_REPO,
                    currentTimeGetter.get()
                )
            )
        }
    }

    override fun listAsFlow() = githubRepoDao.listAsFlow().map { list ->
        list.map {
            GithubRepo(
                it.serverId,
                it.name,
                it.description,
                Date(it.updatedAt),
                it.language,
                it.htmlUrl,
                it.fork
            )
        }
    }

    override suspend fun clear() {
        db.withTransaction {
            githubRepoDao.clear()
            createdDao.delete(LocalCreated.KIND_GITHUB_REPO)
        }
    }
}
