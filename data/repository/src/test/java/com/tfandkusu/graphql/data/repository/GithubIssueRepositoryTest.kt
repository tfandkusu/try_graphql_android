package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = GithubIssueRepositoryImpl(remoteDataStore)
    }

    @Test
    fun listAsFlow() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        every {
            remoteDataStore.listAsFlow(false)
        } returns flow {
            emit(issues)
        }
        repository.listAsFlow().first() shouldBe issues
        verifySequence {
            remoteDataStore.listAsFlow(false)
        }
    }

    @Test
    fun reload() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        every {
            remoteDataStore.listAsFlow(true)
        } returns flow {
            emit(issues)
        }
        repository.reload()
        verifySequence {
            remoteDataStore.listAsFlow(true)
        }
    }

    @Test
    fun get() = runBlocking {
        val issue = GitHubIssueCatalog.getList().last()
        coEvery {
            remoteDataStore.get(false, 1)
        } returns issue
        repository.get(1) shouldBe issue
        coVerifySequence {
            remoteDataStore.get(false, 1)
        }
    }

    @Test
    fun update() = runBlocking {
        val issues = GitHubIssueCatalog.getList()
        val issue = issues.last()
        every {
            remoteDataStore.listAsFlow(true)
        } returns flow {
            emit(issues)
        }
        repository.update(issue)
        coVerifySequence {
            remoteDataStore.update(issue)
            remoteDataStore.listAsFlow(true)
            remoteDataStore.get(true, issue.number)
        }
    }
}
