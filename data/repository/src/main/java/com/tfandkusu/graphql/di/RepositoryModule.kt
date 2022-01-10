package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.data.repository.GithubIssueRepository
import com.tfandkusu.graphql.data.repository.GithubIssueRepositoryImpl
import com.tfandkusu.graphql.data.repository.GithubRepoRepository
import com.tfandkusu.graphql.data.repository.GithubRepoRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindGitHubIssueRepository(
        repository: GithubIssueRepositoryImpl
    ): GithubIssueRepository
}
