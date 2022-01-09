package com.tfandkusu.graphql.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.catalog.GitHubRepoCatalog
import com.tfandkusu.graphql.error.NetworkErrorException
import com.tfandkusu.graphql.usecase.home.HomeLoadUseCase
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.viewmodel.error.ApiErrorState
import com.tfandkusu.graphql.viewmodel.mockStateObserver
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
    private lateinit var loadUseCase: HomeLoadUseCase

    @MockK(relaxed = true)
    private lateinit var onCreateUseCase: HomeOnCreateUseCase

    private lateinit var viewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = HomeViewModelImpl(loadUseCase, onCreateUseCase)
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
        val repos = GitHubRepoCatalog.getList()
        every {
            onCreateUseCase.execute()
        } returns flow {
            emit(repos)
        }
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(HomeEvent.OnCreate)
        verifySequence {
            mockStateObserver.onChanged(HomeState())
            onCreateUseCase.execute()
            mockStateObserver.onChanged(HomeState(repos = repos))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadSuccess() = testDispatcher.runBlockingTest {
        val stateMockObserver = viewModel.state.mockStateObserver()
        val errorStateMockObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(HomeEvent.Load)
        coVerifySequence {
            stateMockObserver.onChanged(HomeState())
            errorStateMockObserver.onChanged(ApiErrorState())
            errorStateMockObserver.onChanged(ApiErrorState())
            stateMockObserver.onChanged(HomeState(progress = true))
            loadUseCase.execute()
            stateMockObserver.onChanged(HomeState(progress = false))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadError() = testDispatcher.runBlockingTest {
        coEvery {
            loadUseCase.execute()
        } throws NetworkErrorException()
        val stateMockObserver = viewModel.state.mockStateObserver()
        val errorMockStateObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(HomeEvent.Load)
        coVerifySequence {
            stateMockObserver.onChanged(HomeState())
            errorMockStateObserver.onChanged(ApiErrorState())
            errorMockStateObserver.onChanged(ApiErrorState())
            stateMockObserver.onChanged(HomeState(progress = true))
            loadUseCase.execute()
            errorMockStateObserver.onChanged(ApiErrorState(network = true))
            stateMockObserver.onChanged(HomeState(progress = false))
        }
    }
}
