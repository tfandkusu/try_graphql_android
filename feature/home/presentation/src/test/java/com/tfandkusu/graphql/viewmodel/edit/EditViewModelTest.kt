package com.tfandkusu.graphql.viewmodel.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.error.NetworkErrorException
import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.usecase.edit.EditDeleteUseCase
import com.tfandkusu.graphql.usecase.edit.EditLoadUseCase
import com.tfandkusu.graphql.usecase.edit.EditLoadUseCaseResult
import com.tfandkusu.graphql.usecase.edit.EditSubmitUseCase
import com.tfandkusu.graphql.viewmodel.error.ApiErrorShowKind
import com.tfandkusu.graphql.viewmodel.error.ApiErrorState
import com.tfandkusu.graphql.viewmodel.mockStateObserver
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    private lateinit var loadUseCase: EditLoadUseCase

    @MockK(relaxed = true)
    private lateinit var deleteUseCase: EditDeleteUseCase

    @MockK(relaxed = true)
    private lateinit var submitUseCase: EditSubmitUseCase

    private lateinit var viewModel: EditViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = EditViewModelImpl(
            loadUseCase,
            deleteUseCase,
            submitUseCase
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
    fun loadSuccessCreate() = testDispatcher.runBlockingTest {
        val result = EditLoadUseCaseResult(
            false,
            GithubIssue(
                "",
                0,
                "",
                false
            )
        )
        coEvery {
            loadUseCase.execute(1)
        } returns result
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.Load(1))
        // This process is executed only once.
        viewModel.event(EditEvent.Load(1))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState())
            loadUseCase.execute(1)
            mockStateObserver.onChanged(
                EditState(
                    false,
                    false,
                    false,
                    "",
                    0,
                    "",
                    false,
                    false
                )
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadSuccessEdit() = testDispatcher.runBlockingTest {
        val issue = GitHubIssueCatalog.getList().last()
        val result = EditLoadUseCaseResult(true, issue)
        coEvery {
            loadUseCase.execute(1)
        } returns result
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.Load(1))
        // This process is executed only once.
        viewModel.event(EditEvent.Load(1))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState())
            loadUseCase.execute(1)
            mockStateObserver.onChanged(
                EditState(
                    true,
                    false,
                    false,
                    "id_1",
                    1,
                    issue.title,
                    issue.closed,
                    true
                )
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadError() = testDispatcher.runBlockingTest {
        coEvery {
            loadUseCase.execute(1)
        } throws NetworkErrorException()
        val mockStateObserver = viewModel.state.mockStateObserver()
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(EditEvent.Load(1))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(EditState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            loadUseCase.execute(1)
            mockErrorStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.SCREEN
                )
            )
            mockStateObserver.onChanged(EditState(progress = false))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun cancelDelete() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.ConfirmDelete)
        viewModel.event(EditEvent.CancelDelete)
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState(confirmDelete = true))
            mockStateObserver.onChanged(EditState())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteSuccess() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.ConfirmDelete)
        viewModel.event(EditEvent.Delete("id_1"))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState(confirmDelete = true))
            mockStateObserver.onChanged(EditState(progress = true))
            deleteUseCase.execute("id_1")
        }
        viewModel.effect.first() shouldBe EditEffect.BackToHome
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteError() = testDispatcher.runBlockingTest {
        coEvery {
            deleteUseCase.execute("id_1")
        } throws NetworkErrorException()
        val mockStateObserver = viewModel.state.mockStateObserver()
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        viewModel.event(EditEvent.Delete("id_1"))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(EditState())
            deleteUseCase.execute("id_1")
            mockErrorStateObserver.onChanged(
                ApiErrorState(
                    network = true,
                    showKind = ApiErrorShowKind.DIALOG
                )
            )
            mockStateObserver.onChanged(EditState(progress = false))
        }
    }

    @Test
    fun updateTitle() {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.UpdateTitle("Updated"))
        viewModel.event(EditEvent.UpdateTitle(""))
        verifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState(title = "Updated", submitEnabled = true))
            mockStateObserver.onChanged(EditState(title = "", submitEnabled = false))
        }
    }

    @Test
    fun updateClosed() {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.UpdateClosed(true))
        verifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState(closed = true))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitSuccess() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        viewModel.event(EditEvent.Submit("id_1", 1, "Title 1", true))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockStateObserver.onChanged(EditState(progress = true))
            submitUseCase.execute(
                GithubIssue(
                    "id_1",
                    1,
                    "Title 1",
                    true
                )
            )
        }
        viewModel.effect.first() shouldBe EditEffect.BackToHome
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitError() = testDispatcher.runBlockingTest {
        val mockStateObserver = viewModel.state.mockStateObserver()
        val mockErrorStateObserver = viewModel.error.state.mockStateObserver()
        val issue = GithubIssue(
            "id_1",
            1,
            "Title 1",
            true
        )
        coEvery {
            submitUseCase.execute(issue)
        } throws NetworkErrorException()
        viewModel.event(EditEvent.Submit("id_1", 1, "Title 1", true))
        coVerifySequence {
            mockStateObserver.onChanged(EditState())
            mockErrorStateObserver.onChanged(ApiErrorState())
            mockStateObserver.onChanged(EditState(progress = true))
            submitUseCase.execute(issue)
            mockErrorStateObserver.onChanged(
                ApiErrorState(network = true, showKind = ApiErrorShowKind.DIALOG)
            )
            mockStateObserver.onChanged(EditState(progress = false))
        }
    }
}
