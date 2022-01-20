package com.tfandkusu.graphql.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tfandkusu.graphql.data.local.db.AppDatabaseBuilder
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CreatedLocalDataStoreTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val db = AppDatabaseBuilder.build(context, true)

    private lateinit var createdLocalDataStore: CreatedLocalDataStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        createdLocalDataStore = CreatedLocalDataStoreImpl(db)
    }

    @Test
    fun test() = runBlocking {
        // Check empty
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE) shouldBe 0L
        // Check save
        createdLocalDataStore.set(LocalCreated.KIND_GITHUB_ISSUE, 1000L)
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE) shouldBe 1000L
        // Check delete
        createdLocalDataStore.delete(LocalCreated.KIND_GITHUB_ISSUE)
        createdLocalDataStore.get(LocalCreated.KIND_GITHUB_ISSUE) shouldBe 0L
    }
}
