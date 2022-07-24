package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.local.CreatedLocalDataStore
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.util.CurrentTimeGetter
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubIssueRepositoryTest {

    private lateinit var repository: GithubIssueRepository

    @MockK(relaxed = true)
    private lateinit var remoteDataStore: GithubIssueRemoteDataStore

    @MockK(relaxed = true)
    private lateinit var createdLocalDataStore: CreatedLocalDataStore

    @MockK(relaxed = true)
    private lateinit var currentTimeGetter: CurrentTimeGetter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = GithubIssueRepositoryImpl(
            remoteDataStore,
            createdLocalDataStore,
            currentTimeGetter
        )
    }

    @Test
    fun listAsFlow() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        every {
            remoteDataStore.listAsFlow()
        } returns flow {
            emit(issues)
        }
        repository.listAsFlow().first() shouldBe issues
        verifySequence {
            remoteDataStore.listAsFlow()
        }
    }

    @Test
    fun fetchReload() = runBlocking {
        val now = 24 * 60 * 60 * 1000L
        every {
            currentTimeGetter.get()
        } returns now
        repository.fetch(true)
        coVerifySequence {
            currentTimeGetter.get()
            remoteDataStore.fetch()
            createdLocalDataStore.set(LocalCreated.KIND_GITHUB_ISSUE, now)
        }
    }

    @Test
    fun fetchUseCache() = runBlocking {
        val now = 24 * 60 * 60 * 1000L
        coEvery {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE)
        } returns now - 10 * 60 * 1000
        every {
            currentTimeGetter.get()
        } returns now
        repository.fetch(false)
        coVerifySequence {
            currentTimeGetter.get()
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE)
        }
        coVerify(exactly = 0) {
            remoteDataStore.fetch()
        }
    }

    @Test
    fun fetchCacheExpired() = runBlocking {
        val now = 24 * 60 * 60 * 1000L
        every {
            currentTimeGetter.get()
        } returns now
        coEvery {
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE)
        } returns now - 10 * 60 * 1000 - 1
        repository.fetch(false)
        coVerifySequence {
            currentTimeGetter.get()
            createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE)
            remoteDataStore.fetch()
            createdLocalDataStore.set(LocalCreated.KIND_GITHUB_ISSUE, now)
        }
    }

    @Test
    fun get() = runBlocking {
        val issue = GitHubIssueCatalog.getList().last()
        coEvery {
            remoteDataStore.get(1)
        } returns issue
        repository.get(1) shouldBe issue
        coVerifySequence {
            remoteDataStore.get(1)
        }
    }

    @Test
    fun update() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        val issue = issues.last()
        repository.update(issue)
        coVerifySequence {
            remoteDataStore.update(issue)
            remoteDataStore.fetch()
        }
    }

    @Test
    fun create() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        val issue = issues.last()
        repository.create(issue)
        coVerifySequence {
            remoteDataStore.create(issue)
            remoteDataStore.fetch()
        }
    }

    @Test
    fun delete() = runBlocking {
        repository.delete("id_1")
        coVerifySequence {
            remoteDataStore.delete("id_1")
            remoteDataStore.fetch()
        }
    }

    @Test
    fun clearCache() = runBlocking {
        repository.clearCache()
        coVerifySequence {
            remoteDataStore.clearCache()
        }
    }
}
