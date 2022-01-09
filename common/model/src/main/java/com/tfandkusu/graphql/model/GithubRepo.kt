package com.tfandkusu.graphql.model

import java.util.Date

data class GithubRepo(
    val id: Long,
    val name: String,
    val description: String,
    val updatedAt: Date,
    val language: String,
    val htmlUrl: String,
    val fork: Boolean
)
