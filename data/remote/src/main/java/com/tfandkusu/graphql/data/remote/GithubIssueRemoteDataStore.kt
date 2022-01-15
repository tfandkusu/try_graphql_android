package com.tfandkusu.graphql.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.tfandkusu.graphql.api.GetIssueQuery
import com.tfandkusu.graphql.api.ListIssuesQuery
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GithubIssueRemoteDataStore {
    fun listAsFlow(networkOnly: Boolean): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?
}

class GithubIssueRemoteDataStoreImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubIssueRemoteDataStore {
    override fun listAsFlow(networkOnly: Boolean): Flow<List<GithubIssue>> {
        val repositoryName = BuildConfig.REPOSITORY_NAME
        val query = ListIssuesQuery(repositoryName)
        return apolloClient.query(query)
            .fetchPolicy(
                if (networkOnly) {
                    FetchPolicy.NetworkOnly
                } else {
                    FetchPolicy.CacheFirst
                }
            ).watch().map { apolloResponse ->
                apolloResponse.data?.viewer?.repository?.issues?.edges?.mapNotNull { edge ->
                    edge?.node
                }?.map {
                    GithubIssue(
                        it.id,
                        it.number,
                        it.title,
                        it.closed
                    )
                } ?: listOf()
            }
    }

    override suspend fun get(number: Int): GithubIssue? {
        val ownerName = BuildConfig.OWNER_NAME
        val repositoryName = BuildConfig.REPOSITORY_NAME
        return apolloClient.query(GetIssueQuery(ownerName, repositoryName, number))
            .execute().data?.let { data ->
                data.repository?.issue?.let {
                    GithubIssue(it.id, it.number, it.title, it.closed)
                }
            }
    }
}
