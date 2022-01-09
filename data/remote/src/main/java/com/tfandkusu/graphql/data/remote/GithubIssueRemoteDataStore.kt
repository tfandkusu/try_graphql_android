package com.tfandkusu.graphql.data.remote

import com.apollographql.apollo3.ApolloClient
import com.tfandkusu.graphql.api.IssuesQuery
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GithubIssueRemoteDataStore {
    suspend fun listIssues(): Flow<List<GithubIssue>>
}

class GithubIssueRemoteDataStoreImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubIssueRemoteDataStore {
    override suspend fun listIssues(): Flow<List<GithubIssue>> {
        val query = IssuesQuery("try_graphql_android")
        return apolloClient.query(query).toFlow().map { apolloResponse ->
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
