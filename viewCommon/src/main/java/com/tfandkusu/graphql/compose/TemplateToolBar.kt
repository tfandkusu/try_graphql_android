package com.tfandkusu.graphql.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.tfandkusu.graphql.viewcommon.R

@Composable
fun TemplateTopAppBar(
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = title,
        actions = actions,
        backgroundColor = colorResource(R.color.top_app_bar)
    )
}
