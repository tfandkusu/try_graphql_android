package com.tfandkusu.graphql.model

data class GithubIssue(
    val id: String,
    val number: Int,
    val title: String,
    val closed: Boolean
)
