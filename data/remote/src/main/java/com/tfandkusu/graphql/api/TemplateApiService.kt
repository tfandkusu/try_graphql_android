package com.tfandkusu.graphql.api

import com.tfandkusu.graphql.api.response.GithubRepoListResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface TemplateApiService {
    @GET("/users/tfandkusu/repos")
    suspend fun listRepos(@Query("page") page: Int): List<GithubRepoListResponseItem>
}
