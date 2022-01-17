package com.tfandkusu.graphql.compose.home.listitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfandkusu.graphql.catalog.GitHubIssueCatalog
import com.tfandkusu.graphql.home.compose.R
import com.tfandkusu.graphql.model.GithubIssue
import com.tfandkusu.graphql.ui.theme.AppTemplateTheme

@Composable
fun GitHubIssueListItem(
    issue: GithubIssue,
    onClick: (number: Int) -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            onClick(issue.number)
        }
    ) {
        // Title
        Text(
            text = issue.title,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = colorResource(R.color.textHE)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        ClosedLabel(issue.closed)
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}

@Composable
fun ClosedLabel(closed: Boolean) {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .background(
                    color = if (closed) {
                        colorResource(R.color.closed)
                    } else {
                        colorResource(R.color.open)
                    },
                    shape = RoundedCornerShape(14.dp, 14.dp, 14.dp, 14.dp)
                )
                .padding(horizontal = 16.dp)
                .height(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (closed) {
                    stringResource(R.string.closed)
                } else {
                    stringResource(R.string.open)
                },
                style = TextStyle(
                    color = colorResource(R.color.white),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GitHubIssueListItemPreviewNormal() {
    AppTemplateTheme {
        GitHubIssueListItem(
            GitHubIssueCatalog.getList().first()
        ) {
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun GitHubIssueListItemPreviewDark() {
    AppTemplateTheme {
        GitHubIssueListItem(
            GitHubIssueCatalog.getList().first().copy(closed = true)
        ) {
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GitHubIssueListItemPreviewLong() {
    AppTemplateTheme {
        GitHubIssueListItem(
            GithubIssue(
                "id",
                1,
                "long_issue_" + (0 until 10).joinToString(separator = "_") {
                    "long"
                },
                false
            )
        ) {
        }
    }
}
