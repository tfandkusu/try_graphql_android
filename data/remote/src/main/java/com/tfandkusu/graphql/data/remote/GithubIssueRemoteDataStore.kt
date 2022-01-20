package com.tfandkusu.graphql.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.exception.ApolloException
import com.tfandkusu.graphql.api.GetIssueQuery
import com.tfandkusu.graphql.api.ListIssuesQuery
import com.tfandkusu.graphql.api.UpdateIssueStateMutation
import com.tfandkusu.graphql.api.UpdateIssueTitleMutation
import com.tfandkusu.graphql.api.type.IssueState
import com.tfandkusu.graphql.model.GithubIssue
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

interface GithubIssueRemoteDataStore {

    suspend fun fetch()

    fun listAsFlow(): Flow<List<GithubIssue>>

    suspend fun get(networkOnly: Boolean, number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)
}

class GithubIssueRemoteDataStoreImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubIssueRemoteDataStore {

    override suspend fun fetch() {
        val repositoryName = BuildConfig.REPOSITORY_NAME
        val query = ListIssuesQuery(repositoryName)
        try {
            apolloClient.query(query).fetchPolicy(FetchPolicy.NetworkOnly).execute()
        } catch (e: ApolloException) {
            Timber.d(e)
        }
    }

    override fun listAsFlow(): Flow<List<GithubIssue>> {
        val repositoryName = BuildConfig.REPOSITORY_NAME
        val query = ListIssuesQuery(repositoryName)
        return apolloClient.query(query)
            .fetchPolicy(
                FetchPolicy.CacheOnly
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

    override suspend fun get(networkOnly: Boolean, number: Int): GithubIssue? {
        val ownerName = BuildConfig.OWNER_NAME
        val repositoryName = BuildConfig.REPOSITORY_NAME
        return apolloClient.query(GetIssueQuery(ownerName, repositoryName, number))
            .fetchPolicy(
                if (networkOnly) {
                    FetchPolicy.NetworkOnly
                } else {
                    FetchPolicy.CacheFirst
                }
            )
            .execute().data?.let { data ->
                data.repository?.issue?.let {
                    GithubIssue(it.id, it.number, it.title, it.closed)
                }
            }
    }

    override suspend fun update(issue: GithubIssue) {
        apolloClient.mutation(
            UpdateIssueTitleMutation(
                issue.id,
                issue.title,
            )
        ).execute()
        apolloClient.mutation(
            UpdateIssueStateMutation(
                issue.id,
                if (issue.closed) {
                    IssueState.CLOSED
                } else {
                    IssueState.OPEN
                },
            )
        ).execute()
    }
}
