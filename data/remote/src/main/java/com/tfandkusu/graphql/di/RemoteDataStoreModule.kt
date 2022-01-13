package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStore
import com.tfandkusu.graphql.data.remote.GithubIssueRemoteDataStoreImpl
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
    abstract fun provideGithubIssueRemoteDataStore(
        remoteDataStore: GithubIssueRemoteDataStoreImpl
    ): GithubIssueRemoteDataStore
}
