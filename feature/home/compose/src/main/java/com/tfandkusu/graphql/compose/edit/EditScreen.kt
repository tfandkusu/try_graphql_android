package com.tfandkusu.graphql.compose.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tfandkusu.graphql.compose.TemplateTopAppBar
import com.tfandkusu.graphql.home.compose.R
import com.tfandkusu.graphql.viewmodel.edit.EditEvent
import com.tfandkusu.graphql.viewmodel.edit.EditViewModel
import com.tfandkusu.graphql.viewmodel.useState

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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
