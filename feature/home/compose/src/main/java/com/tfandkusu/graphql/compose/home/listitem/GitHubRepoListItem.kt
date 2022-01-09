package com.tfandkusu.graphql.compose.home.listitem

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfandkusu.graphql.catalog.GitHubRepoCatalog
import com.tfandkusu.graphql.home.compose.R
import com.tfandkusu.graphql.model.GithubRepo
import com.tfandkusu.graphql.ui.theme.AppTemplateTheme
import java.util.Date

@Composable
fun GitHubRepoListItem(repo: GithubRepo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(repo.htmlUrl)
            context.startActivity(intent)
        }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Name
            Text(
                text = repo.name,
                modifier = Modifier.weight(1f, false),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = colorResource(R.color.textHE)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Fork label
            if (repo.fork) {
                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(R.color.forkBackground),
                            shape = RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .height(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.fork),
                        style = TextStyle(
                            color = colorResource(R.color.white),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        // Description
        if (repo.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = repo.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.textME)
                ),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language label
            if (repo.language.isNotEmpty()) {
                LanguageLabel(repo.language)
            }
            // Update time
            val format = DateFormat.getDateFormat(context)
            val dateString = format.format(repo.updatedAt)
            Text(
                dateString,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = colorResource(R.color.textME),
                    textAlign = TextAlign.End
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}

@Composable
fun LanguageLabel(language: String) {
    val languages = stringArrayResource(R.array.programing_languages)
    val colorCodes = integerArrayResource(R.array.programing_language_colors)
    var color = colorResource(R.color.other)
    val languageIndex = languages.indexOf(language)
    if (languageIndex >= 0) {
        color = Color(colorCodes[languageIndex])
    }
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(14.dp, 14.dp, 14.dp, 14.dp)
                )
                .padding(horizontal = 16.dp)
                .height(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language,
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
fun GitHubRepoListItemPreviewNormal() {
    AppTemplateTheme {
        GitHubRepoListItem(
            GitHubRepoCatalog.getList().first()
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun GitHubRepoListItemPreviewDark() {
    AppTemplateTheme {
        GitHubRepoListItem(
            GitHubRepoCatalog.getList().first()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun GitHubRepoListItemPreviewLong() {
    AppTemplateTheme {
        GitHubRepoListItem(
            GithubRepo(
                1L,
                "long_repository_" + (0 until 10).joinToString(separator = "_") {
                    "long"
                },
                listOf(
                    "Check how to use Room to observe SQLite database",
                    " and reflect the changes in the RecyclerView."
                ).joinToString(separator = ""),
                Date(),
                "Kotlin",
                "",
                true
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun GitHubRepoListItemNoDescription() {
    AppTemplateTheme {
        GitHubRepoListItem(
            GithubRepo(
                1L,
                "no_description",
                "",
                Date(),
                "Kotlin",
                "",
                true
            )
        )
    }
}
