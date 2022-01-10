package com.tfandkusu.graphql.catalog

import com.tfandkusu.graphql.model.GithubIssue

object GitHubIssueCatalog {
    fun getList(): List<GithubIssue> {
        return listOf(
            GithubIssue(
                "id_3",
                3,
                "Issue 3",
                false
            ),
            GithubIssue(
                "id_2",
                2,
                "Issue 2",
                false
            ),
            GithubIssue(
                "id_1",
                1,
                "Issue 1",
                true
            )
        )
    }
}
