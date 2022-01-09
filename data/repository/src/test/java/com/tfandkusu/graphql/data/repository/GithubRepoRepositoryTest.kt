package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.catalog.GitHubRepoCatalog
import com.tfandkusu.graphql.data.local.CreatedLocalDataStore
import com.tfandkusu.graphql.data.local.GithubRepoLocalDataStore
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import com.tfandkusu.graphql.data.remote.GithubRemoteDataStore
import com.tfandkusu.graphql.model.GithubRepo
import com.tfandkusu.graphql.util.CurrentTimeGetter
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubRepoRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var remoteDataStore: GithubRemoteDataStore

    @MockK(relaxed = true)
    private lateinit var localDataStore: GithubRepoLocalDataStore

    @MockK(relaxed = true)
    private lateinit var createdLocalDataStore: CreatedLocalDataStore

    @MockK(relaxed = true)
    private lateinit var currentTimeGetter: CurrentTimeGetter

    private lateinit var repository: GithubRepoRepository

    /**
     * Sample data
     */
    private lateinit var repos: List<GithubRepo>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = GithubRepoRepositoryImpl(
            remoteDataStore,
            localDataStore,
            createdLocalDataStore,
            currentTimeGetter
        )
        this.repos = GitHubRepoCatalog.getList()
    }

    @Test
    fun isCacheExpiredTrue() = runBlocking {
        coEvery {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO)
        } returns 1 * 60 * 60 * 1000L
        every {
            currentTimeGetter.get()
        } returns 2 * 60 * 60 * 1000L + 1
        repository.isCacheExpired() shouldBe true
        coVerifySequence {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO)
            currentTimeGetter.get()
        }
    }

    @Test
    fun isCacheExpiredFalse() = runBlocking {
        coEvery {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO)
        } returns 1 * 60 * 60 * 1000L
        every {
            currentTimeGetter.get()
        } returns 2 * 60 * 60 * 1000L
        repository.isCacheExpired() shouldBe false
        coVerifySequence {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO)
            currentTimeGetter.get()
        }
    }

    @Test
    fun fetch() = runBlocking {
        coEvery {
            remoteDataStore.listRepositories()
        } returns repos
        repository.fetch()
        coVerifySequence {
            remoteDataStore.listRepositories()
            localDataStore.save(repos)
        }
    }

    @Test
    fun listAsFlow() = runBlocking {
        every {
            localDataStore.listAsFlow()
        } returns flow {
            emit(repos)
        }
        repository.listAsFlow().first() shouldBe repos
        coVerifySequence {
            localDataStore.listAsFlow()
        }
    }
}
