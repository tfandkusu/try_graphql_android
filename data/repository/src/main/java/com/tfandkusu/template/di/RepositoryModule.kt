package com.tfandkusu.template.di

import com.tfandkusu.template.data.repository.GithubRepoRepository
import com.tfandkusu.template.data.repository.GithubRepoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGithubRepoRepository(
        repository: GithubRepoRepositoryImpl
    ): GithubRepoRepository
}
