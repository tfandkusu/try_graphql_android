package com.tfandkusu.graphql.data.repository

import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubIssueRepositoryTest {

    private lateinit var repository: GithubIssueRepository

    @MockK
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
            remoteDataStore.listAsFlow()
        } returns flow {
            emit(issues)
        }
        repository.listAsFlow().first() shouldBe issues
    }
}