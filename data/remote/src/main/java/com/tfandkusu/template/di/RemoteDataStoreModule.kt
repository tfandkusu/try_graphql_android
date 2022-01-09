package com.tfandkusu.template.di

import com.tfandkusu.template.data.remote.GithubRemoteDataStore
import com.tfandkusu.template.data.remote.GithubRemoteDataStoreImpl
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
        localDataStore: GithubRemoteDataStoreImpl
    ): GithubRemoteDataStore
}
