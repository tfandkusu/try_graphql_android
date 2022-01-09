package com.tfandkusu.template.api

import com.tfandkusu.template.api.response.GithubRepoListResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface TemplateApiService {
    @GET("/users/tfandkusu/repos")
    suspend fun listRepos(@Query("page") page: Int): List<GithubRepoListResponseItem>
}
