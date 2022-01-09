package com.tfandkusu.template.data.remote

import com.tfandkusu.template.api.TemplateApiService
import com.tfandkusu.template.error.mapApiError
import com.tfandkusu.template.model.GithubRepo
import javax.inject.Inject

interface GithubRemoteDataStore {
    suspend fun listRepositories(): List<GithubRepo>
}

class GithubRemoteDataStoreImpl @Inject constructor(
    private val service: TemplateApiService
) : GithubRemoteDataStore {
    override suspend fun listRepositories(): List<GithubRepo> {
        val allRepos = mutableListOf<GithubRepo>()
        var page = 1
        while (true) {
            try {
                val response = service.listRepos(page)
                val pageRepos = response.map {
                    GithubRepo(
                        it.id,
                        it.name,
                        it.description ?: "",
                        it.updatedAt,
                        it.language ?: "",
                        it.htmlUrl,
                        it.fork
                    )
                }
                if (pageRepos.isEmpty()) {
                    break
                } else {
                    allRepos.addAll(pageRepos)
                    ++page
                }
            } catch (e: Throwable) {
                throw mapApiError(e)
            }
        }
        return allRepos
    }
}
