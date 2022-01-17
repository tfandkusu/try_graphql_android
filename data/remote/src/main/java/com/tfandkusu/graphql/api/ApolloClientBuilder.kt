package com.tfandkusu.graphql.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.tfandkusu.graphql.data.remote.BuildConfig
import okhttp3.OkHttpClient

object ApolloClientBuilder {
    fun build(): ApolloClient {
        val memoryCacheFactory = MemoryCacheFactory(
            maxSizeBytes = 10 * 1024 * 1024,
            expireAfterMillis = 10 * 60 * 1000
        )
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
            .normalizedCache(memoryCacheFactory)
            .build()
    }
}
