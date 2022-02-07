package com.tfandkusu.graphql.compose.home

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.compose.TemplateTopAppBar
import com.tfandkusu.graphql.compose.home.listitem.GitHubIssueListItem
import com.tfandkusu.graphql.home.compose.R
import com.tfandkusu.graphql.ui.theme.AppTemplateTheme
import com.tfandkusu.graphql.view.error.ApiErrorOnScreen
import com.tfandkusu.graphql.view.info.InfoActivityAlias
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper
import com.tfandkusu.graphql.viewmodel.error.useErrorState
import com.tfandkusu.graphql.viewmodel.home.HomeEffect
import com.tfandkusu.graphql.viewmodel.home.HomeEvent
import com.tfandkusu.graphql.viewmodel.home.HomeState
import com.tfandkusu.graphql.viewmodel.home.HomeViewModel
import com.tfandkusu.graphql.viewmodel.useState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    editIssue: (number: Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.event(HomeEvent.Load)
        viewModel.event(HomeEvent.OnCreate)
    }
    val context = LocalContext.current
    val state = useState(viewModel)
    val errorState = useErrorState(viewModel.error)
    Scaffold(
        topBar = {
            TemplateTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, InfoActivityAlias::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.action_information),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editIssue(0)
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.home_add))
            }
        }
    ) {
        if (errorState.hasErrorOnScreen()) {
            ApiErrorOnScreen(errorState) {
                viewModel.event(HomeEvent.Load)
            }
        } else {
            if (state.progress) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(state.refresh),
                    onRefresh = {
                        viewModel.event(HomeEvent.Reload)
                    }
                ) {
                    LazyColumn {
                        state.issues.map {
                            item(key = it.id) {
                                GitHubIssueListItem(it) { number ->
                                    editIssue(number)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class HomeViewModelPreview(private val previewState: HomeState) : HomeViewModel {
    override val error: ApiErrorViewModelHelper
        get() = ApiErrorViewModelHelper()

    override fun createDefaultState() = previewState

    override val state: LiveData<HomeState>
        get() = MutableLiveData(createDefaultState())

    override val effect: Flow<HomeEffect>
        get() = flow {}

    override fun event(event: HomeEvent) {
    }
}

@Composable
@Preview
fun HomeScreenPreviewProgress() {
    AppTemplateTheme {
        HomeScreen(HomeViewModelPreview(HomeState())) {
        }
    }
}

@Composable
@Preview
fun HomeScreenPreviewList() {
    val issues = GitHubIssueCatalog.getList()
    val state = HomeState(
        progress = false,
        issues = issues
    )
    AppTemplateTheme {
        HomeScreen(HomeViewModelPreview(state)) {
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomeScreenPreviewDarkProgress() {
    AppTemplateTheme {
        HomeScreen(HomeViewModelPreview(HomeState())) {
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomeScreenPreviewDarkList() {
    val issues = GitHubIssueCatalog.getList()
    val state = HomeState(
        progress = false,
        issues = issues
    )
    AppTemplateTheme {
        HomeScreen(HomeViewModelPreview(state)) {
        }
    }
}
