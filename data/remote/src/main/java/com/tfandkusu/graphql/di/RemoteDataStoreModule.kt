package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStoreImpl
import com.tfandkusu.graphql.data.remote.GithubRemoteDataStore
import com.tfandkusu.graphql.data.remote.GithubRemoteDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataStoreModule {
    @Binds
    @Singleton
    abstract fun provideGithubRemoteDataStore(
        remoteDataStore: GithubRemoteDataStoreImpl
    ): GithubRemoteDataStore

    @Binds
    @Singleton
    abstract fun provideGithubIssueRemoteDataStore(
        remoteDataStore: GithubIssueRemoteDataStoreImpl
    ): GithubIssueRemoteDataStore
}
