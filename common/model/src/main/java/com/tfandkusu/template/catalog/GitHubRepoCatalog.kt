package com.tfandkusu.template.catalog

import com.tfandkusu.template.model.GithubRepo
import com.tfandkusu.template.util.parseUTC

object GitHubRepoCatalog {
    fun getList(): List<GithubRepo> {
        val repo1 = GithubRepo(
            229475311L,
            "observe_room",
            listOf(
                "Check how to use Room to observe SQLite database ",
                "and reflect the changes in the RecyclerView."
            ).joinToString(separator = ""),
            parseUTC("2021-10-29T00:15:46Z"),
            "Kotlin",
            "https://github.com/tfandkusu/observe_room",
            false
        )
        val repo2 = GithubRepo(
            343133709L,
            "conference-app-2021",
            "The Official App for DroidKaigi 2021",
            parseUTC("2021-09-21T16:56:04Z"),
            "Kotlin",
            "https://github.com/tfandkusu/conference-app-2021",
            true
        )
        val repo3 = GithubRepo(
            320900929L,
            "groupie_sticky_header_sample",
            "Sample app for sticky header on the groupie",
            parseUTC("2021-01-19T19:46:27Z"),
            "Java",
            "https://github.com/tfandkusu/groupie_sticky_header_sample",
            false
        )
        return listOf(repo1, repo2, repo3)
    }
}
