package com.tfandkusu.template.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tfandkusu.template.catalog.GitHubRepoCatalog
import com.tfandkusu.template.data.local.db.AppDatabaseBuilder
import com.tfandkusu.template.data.local.entity.LocalCreated
import com.tfandkusu.template.util.CurrentTimeGetter
import com.tfandkusu.template.util.parseUTC
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GithubRepoLocalDataStoreTest {
    @MockK(relaxed = true)
    private lateinit var currentTimeGetter: CurrentTimeGetter

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val db = AppDatabaseBuilder.build(context, true)

    private lateinit var localDataStore: GithubRepoLocalDataStore

    private lateinit var createdLocalDataStore: CreatedLocalDataStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localDataStore = GithubRepoLocalDataStoreImpl(db, currentTimeGetter)
        createdLocalDataStore = CreatedLocalDataStoreImpl(db)
    }

    @Test
    fun test() = runBlocking {
        // Check empty
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO) shouldBe 0L
        localDataStore.listAsFlow().first() shouldBe listOf()
        // Check save
        val firstTime = parseUTC("2021-11-15T01:12:45Z")
        val secondTime = parseUTC("2021-11-15T03:00:00Z")
        every {
            currentTimeGetter.get()
        } returns firstTime.time andThen secondTime.time
        val (repo1, repo2, repo3) = GitHubRepoCatalog.getList()
        localDataStore.save(listOf(repo1, repo3, repo2))
        localDataStore.listAsFlow().first() shouldBe listOf(repo1, repo2, repo3)
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO) shouldBe firstTime.time
        // Second save
        localDataStore.save(listOf(repo1.copy(name = "Edited"), repo3, repo2))
        localDataStore.listAsFlow().first() shouldBe listOf(
            repo1.copy(name = "Edited"),
            repo2,
            repo3
        )
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO) shouldBe secondTime.time
        // Check clear
        localDataStore.clear()
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_REPO) shouldBe 0
    }
}
