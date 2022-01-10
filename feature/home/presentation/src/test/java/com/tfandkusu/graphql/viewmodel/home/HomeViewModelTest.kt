package com.tfandkusu.graphql.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.usecase.home.HomeReloadUseCase
import com.tfandkusu.graphql.viewmodel.mockStateObserver
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @MockK(relaxed = true)
    private lateinit var onCreateUseCase: HomeOnCreateUseCase

    @MockK(relaxed = true)
    private lateinit var reloadUseCase: HomeReloadUseCase

    private lateinit var viewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = HomeViewModelImpl(onCreateUseCase, reloadUseCase)
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
        val issues = GitHubIssueCatalog.getList()
        every {
            onCreateUseCase.execute()
        } returns flow {
            emit(issues)
        }
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(HomeEvent.OnCreate)
        verifySequence {
            mockStateObserver.onChanged(HomeState())
            onCreateUseCase.execute()
            mockStateObserver.onChanged(HomeState(issues = issues, progress = false))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun reload() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(HomeEvent.Reload)
        coVerifySequence {
            mockStateObserver.onChanged(HomeState())
            mockStateObserver.onChanged(HomeState(refresh = true))
            reloadUseCase.execute()
            mockStateObserver.onChanged(HomeState(refresh = false))
        }
    }
}
