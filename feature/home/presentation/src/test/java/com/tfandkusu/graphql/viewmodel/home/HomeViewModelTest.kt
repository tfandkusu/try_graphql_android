package com.tfandkusu.graphql.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.error.NetworkErrorException
import com.tfandkusu.graphql.usecase.home.HomeLoadUseCase
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.usecase.home.HomeReloadUseCase
import com.tfandkusu.graphql.viewmodel.error.ApiErrorShowKind
import com.tfandkusu.graphql.viewmodel.error.ApiErrorState
import com.tfandkusu.graphql.viewmodel.mockStateObserver
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
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
    private lateinit var loadUseCase: HomeLoadUseCase

    @MockK(relaxed = true)
    private lateinit var reloadUseCase: HomeReloadUseCase

    private lateinit var viewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = HomeViewModelImpl(onCreateUseCase, loadUseCase, reloadUseCase)
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
        coEvery {
            onCreateUseCase.execute()
        } returns flow {
            emit(issues)
        }
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(HomeEvent.OnCreate)
        // called only once
        viewModel.event(HomeEvent.OnCreate)
        coVerifySequence {
            mockStateObserver.onChanged(HomeState())
            onCreateUseCase.execute()
            mockStateObserver.onChanged(HomeState(issues = issues, progress = false))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadSuccess() = testDispatcher.runBlockingTest {
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(HomeEvent.Load)
        coVerifySequence {
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            loadUseCase.execute()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadError() = testDispatcher.runBlockingTest {
        coEvery {
            loadUseCase.execute()
        } throws NetworkErrorException()
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(HomeEvent.Load)
        coVerifySequence {
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            loadUseCase.execute()
            mockErrorStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun reloadSuccess() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(HomeEvent.Reload)
        coVerifySequence {
            mockStateObserver.onChanged(HomeState())
            mockStateObserver.onChanged(HomeState(refresh = true))
            reloadUseCase.execute()
            mockStateObserver.onChanged(HomeState(refresh = false))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun reloadError() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        coEvery {
            reloadUseCase.execute()
        } throws NetworkErrorException()
        viewModel.event(HomeEvent.Reload)
        coVerifySequence {
            mockStateObserver.onChanged(HomeState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(HomeState(refresh = true))
            reloadUseCase.execute()
            mockErrorStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
            mockStateObserver.onChanged(HomeState(refresh = false))
        }
    }
}
