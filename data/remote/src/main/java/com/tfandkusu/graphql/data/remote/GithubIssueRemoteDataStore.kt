package com.tfandkusu.graphql.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.tfandkusu.graphql.api.IssuesQuery
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GithubIssueRemoteDataStore {
    fun listAsFlow(networkOnly: Boolean): Flow<List<GithubIssue>>
}

class GithubIssueRemoteDataStoreImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubIssueRemoteDataStore {
    override fun listAsFlow(networkOnly: Boolean): Flow<List<GithubIssue>> {
        val repositoryName = BuildConfig.REPOSITORY_NAME
        val query = IssuesQuery(repositoryName)
        return apolloClient.query(query)
            .fetchPolicy(
                if (networkOnly) {
                    FetchPolicy.NetworkOnly
                } else {
                    FetchPolicy.CacheFirst
                }
            )
            .toFlow().map { apolloResponse ->
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
}
