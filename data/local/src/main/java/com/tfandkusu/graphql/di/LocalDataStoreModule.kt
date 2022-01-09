package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.data.local.CreatedLocalDataStore
import com.tfandkusu.graphql.data.local.CreatedLocalDataStoreImpl
import com.tfandkusu.graphql.data.local.GithubRepoLocalDataStore
import com.tfandkusu.graphql.data.local.GithubRepoLocalDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataStoreModule {
    @Binds
    @Singleton
    abstract fun provideGithubRepoLocalDataStore(
        localDataStore: GithubRepoLocalDataStoreImpl
    ): GithubRepoLocalDataStore

    @Binds
    @Singleton
    abstract fun provideCreatedLocalDataStore(
        localDataStore: CreatedLocalDataStoreImpl
    ): CreatedLocalDataStore
}
