package com.tfandkusu.graphql.api

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.okHttpClient
import com.tfandkusu.graphql.data.remote.BuildConfig
import okhttp3.OkHttpClient

object ApolloClientBuilder {
    fun build(context: Context): ApolloClient {
        val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory(context)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val githubToken = BuildConfig.GITHUB_TOKEN
                val request = chain.request()
                val newRequest = request.newBuilder().addHeader(
                    "Authorization", "Bearer $githubToken"
                ).build()
                chain.proceed(newRequest)
            }.build()
        return ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl("https://api.github.com/graphql")
            .normalizedCache(sqlNormalizedCacheFactory)
            .build()
    }
}
