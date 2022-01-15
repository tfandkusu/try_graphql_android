package com.tfandkusu.graphql.viewmodel.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.usecase.home.EditOnCreateUseCase
import com.tfandkusu.graphql.viewmodel.mockStateObserver
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class EditViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @MockK(relaxed = true)
    private lateinit var onCreateUseCase: EditOnCreateUseCase

    private lateinit var viewModel: EditViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = EditViewModelImpl(
            onCreateUseCase
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun onCreate() = testDispatcher.runBlockingTest {
        val issue = GitHubIssueCatalog.getList().last()
        coEvery {
            onCreateUseCase.execute(1)
        } returns issue
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.OnCreate(1))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            onCreateUseCase.execute(1)
            mockStateObserver.onChanged(
                EditState(
                    false,
                    1,
                    issue.title,
                    issue.closed,
                    true
                )
            )
        }
    }
}
