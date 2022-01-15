package com.tfandkusu.graphql.compose.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import com.tfandkusu.graphql.viewmodel.edit.EditEffect
import com.tfandkusu.graphql.viewmodel.edit.EditEvent
import com.tfandkusu.graphql.viewmodel.edit.EditState
import com.tfandkusu.graphql.viewmodel.edit.EditViewModel
import com.tfandkusu.graphql.viewmodel.useState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun EditScreen(viewModel: EditViewModel, number: Int) {
    LaunchedEffect(Unit) {
        viewModel.event(EditEvent.OnCreate(number))
    }
    val state = useState(viewModel)
    Scaffold(
        topBar = {
            TemplateTopAppBar(
                title = {
                    Text(stringResource(R.string.title_edit, number))
                }
            )
        }
    ) {
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
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
        }
    }
}

@Composable
@Preview
fun EditScreenPreview() {
    val issue = GitHubIssueCatalog.getList().last()
    val state = EditState(
        progress = false,
        issue.number,
        issue.title,
        issue.closed,
        true,
    )
    AppTemplateTheme {
        EditScreen(EditViewModelPreview(state), issue.number)
    }
}

class EditViewModelPreview(private val previewState: EditState) : EditViewModel {
    override fun createDefaultState() = previewState

    override val state: LiveData<EditState>
        get() = MutableLiveData(createDefaultState())
    override val effect: Flow<EditEffect>
        get() = flow {}

    override fun event(event: EditEvent) {
    }
}
