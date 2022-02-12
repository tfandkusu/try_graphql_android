package com.tfandkusu.graphql.compose.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.compose.TemplateTopAppBar
import com.tfandkusu.graphql.home.compose.R
import com.tfandkusu.graphql.ui.theme.AppTemplateTheme
import com.tfandkusu.graphql.view.error.ApiErrorOnDialog
import com.tfandkusu.graphql.view.error.ApiErrorOnScreen
import com.tfandkusu.graphql.viewmodel.edit.EditEffect
import com.tfandkusu.graphql.viewmodel.edit.EditEvent
import com.tfandkusu.graphql.viewmodel.edit.EditState
import com.tfandkusu.graphql.viewmodel.edit.EditViewModel
import com.tfandkusu.graphql.viewmodel.error.ApiErrorViewModelHelper
import com.tfandkusu.graphql.viewmodel.error.useErrorState
import com.tfandkusu.graphql.viewmodel.useState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Composable
fun EditScreen(viewModel: EditViewModel, number: Int, backToHome: () -> Unit) {
    val state = useState(viewModel)
    val errorState = useErrorState(viewModel.error)
    LaunchedEffect(Unit) {
        viewModel.event(EditEvent.Load(number))
        viewModel.effect.collect { effect ->
            when (effect) {
                EditEffect.BackToHome -> {
                    backToHome()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TemplateTopAppBar(
                title = {
                    Text(
                        if (number == 0) {
                            stringResource(R.string.title_edit_add)
                        } else {
                            stringResource(R.string.title_edit_update, number)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!state.progress) {
                            backToHome()
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.action_back_to_home)
                        )
                    }
                },
            )
        }
    ) {
        if (errorState.hasErrorOnScreen()) {
            ApiErrorOnScreen(errorState) {
                viewModel.event(EditEvent.Load(number))
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
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        value = state.title,
                        onValueChange = { viewModel.event(EditEvent.UpdateTitle(it)) },
                        label = { Text(stringResource(R.string.edit_label_title)) }
                    )
                    if (state.editMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                stringResource(R.string.edit_label_closed)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Switch(
                                checked = state.closed,
                                onCheckedChange = {
                                    viewModel.event(EditEvent.UpdateClosed(it))
                                },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.event(
                                EditEvent.Submit(
                                    state.id,
                                    state.number,
                                    state.title,
                                    state.closed
                                )
                            )
                        },
                        enabled = state.submitEnabled
                    ) {
                        Text(
                            if (state.editMode) {
                                stringResource(R.string.edit_update)
                            } else {
                                stringResource(R.string.edit_add)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
    ApiErrorOnDialog(viewModel.error, errorState)
}

@Composable
@Preview
fun EditScreenPreviewCreate() {
    val state = EditState(
        false,
        false,
        "",
        0,
        "",
        false,
        false
    )
    AppTemplateTheme {
        EditScreen(EditViewModelPreview(state), 0) {
        }
    }
}

@Composable
@Preview
fun EditScreenPreviewUpdate() {
    val issue = GitHubIssueCatalog.getList().last()
    val state = EditState(
        true,
        false,
        "id_1",
        issue.number,
        issue.title,
        issue.closed,
        true,
    )
    AppTemplateTheme {
        EditScreen(EditViewModelPreview(state), issue.number) {
        }
    }
}

class EditViewModelPreview(private val previewState: EditState) : EditViewModel {
    override val error: ApiErrorViewModelHelper
        get() = ApiErrorViewModelHelper()

    override fun createDefaultState() = previewState

    override val state: LiveData<EditState>
        get() = MutableLiveData(createDefaultState())
    override val effect: Flow<EditEffect>
        get() = flow {}

    override fun event(event: EditEvent) {
    }
}
