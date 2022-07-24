package com.tfandkusu.graphql.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.apolloStore
import com.apollographql.apollo3.cache.normalized.doNotStore
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.exception.ApolloException
import com.tfandkusu.graphql.api.CreateIssueMutation
import com.tfandkusu.graphql.api.DeleteIssueMutation
import com.tfandkusu.graphql.api.GetIssueQuery
import com.tfandkusu.graphql.api.GetRepositoryQuery
import com.tfandkusu.graphql.api.ListIssuesQuery
import com.tfandkusu.graphql.api.UpdateIssueStateMutation
import com.tfandkusu.graphql.api.UpdateIssueTitleMutation
import com.tfandkusu.graphql.api.type.IssueState
import com.tfandkusu.graphql.error.ApiErrorMapper
import com.tfandkusu.graphql.model.GithubIssue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GithubIssueRemoteDataStore {

    suspend fun fetch()

    fun listAsFlow(): Flow<List<GithubIssue>>

    suspend fun get(number: Int): GithubIssue?

    suspend fun update(issue: GithubIssue)

    suspend fun create(issue: GithubIssue)

    suspend fun delete(id: String)

    suspend fun clearCache()
}

class GithubIssueRemoteDataStoreImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : GithubIssueRemoteDataStore {

    private val errorHelper = ApiErrorMapper()

    override suspend fun fetch() {
        val repositoryName = BuildConfig.REPOSITORY_NAME
        val query = ListIssuesQuery(repositoryName)
        try {
            apolloClient.query(query).fetchPolicy(FetchPolicy.NetworkOnly).execute()
        } catch (e: ApolloException) {
            throw errorHelper.mapError(e)
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

    override suspend fun get(number: Int): GithubIssue? {
        val ownerName = BuildConfig.OWNER_NAME
        val repositoryName = BuildConfig.REPOSITORY_NAME
        try {
            return apolloClient.query(GetIssueQuery(ownerName, repositoryName, number))
                .fetchPolicy(
                    FetchPolicy.NetworkOnly
                ).doNotStore(true)
                .execute().data?.let { data ->
                    data.repository?.issue?.let {
                        GithubIssue(it.id, it.number, it.title, it.closed)
                    }
                }
        } catch (e: ApolloException) {
            throw errorHelper.mapError(e)
        }
    }

    override suspend fun update(issue: GithubIssue) {
        try {
            apolloClient.mutation(
                UpdateIssueTitleMutation(
                    issue.id,
                    issue.title
                )
            ).doNotStore(true).execute()
            apolloClient.mutation(
                UpdateIssueStateMutation(
                    issue.id,
                    if (issue.closed) {
                        IssueState.CLOSED
                    } else {
                        IssueState.OPEN
                    }
                )
            ).doNotStore(true).execute()
        } catch (e: ApolloException) {
            throw errorHelper.mapError(e)
        }
    }

    override suspend fun create(issue: GithubIssue) {
        val ownerName = BuildConfig.OWNER_NAME
        val repositoryName = BuildConfig.REPOSITORY_NAME
        try {
            val response = apolloClient.query(
                GetRepositoryQuery(
                    ownerName,
                    repositoryName
                )
            ).execute()
            response.data?.repository?.id?.let { repositoryId ->
                apolloClient.mutation(
                    CreateIssueMutation(repositoryId, issue.title)
                ).doNotStore(
                    true
                ).execute()
            }
        } catch (e: ApolloException) {
            throw errorHelper.mapError(e)
        }
    }

    override suspend fun delete(id: String) {
        try {
            apolloClient.mutation(DeleteIssueMutation(id)).execute()
        } catch (e: ApolloException) {
            throw errorHelper.mapError(e)
        }
    }

    override suspend fun clearCache() {
        apolloClient.apolloStore.clearAll()
    }
}
