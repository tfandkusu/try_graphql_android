package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.usecase.edit.EditDeleteUseCase
import com.tfandkusu.graphql.usecase.edit.EditDeleteUseCaseImpl
import com.tfandkusu.graphql.usecase.edit.EditLoadUseCase
import com.tfandkusu.graphql.usecase.edit.EditLoadUseCaseImpl
import com.tfandkusu.graphql.usecase.edit.EditSubmitUseCase
import com.tfandkusu.graphql.usecase.edit.EditSubmitUseCaseImpl
import com.tfandkusu.graphql.usecase.home.HomeLoadUseCase
import com.tfandkusu.graphql.usecase.home.HomeLoadUseCaseImpl
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCase
import com.tfandkusu.graphql.usecase.home.HomeOnCreateUseCaseImpl
import com.tfandkusu.graphql.usecase.home.HomeReloadUseCase
import com.tfandkusu.graphql.usecase.home.HomeReloadUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    @Singleton
    abstract fun bindHomeOnCreateUseCase(
        useCase: HomeOnCreateUseCaseImpl
    ): HomeOnCreateUseCase

    @Binds
    @Singleton
    abstract fun bindHomeLoadUseCase(
        useCase: HomeLoadUseCaseImpl
    ): HomeLoadUseCase

    @Binds
    @Singleton
    abstract fun bindHomeReloadUseCase(
        useCase: HomeReloadUseCaseImpl
    ): HomeReloadUseCase

    @Binds
    @Singleton
    abstract fun bindEditOnCreateUseCase(
        useCase: EditLoadUseCaseImpl
    ): EditLoadUseCase

    @Binds
    @Singleton
    abstract fun bindEditSubmitUseCase(
        useCase: EditSubmitUseCaseImpl
    ): EditSubmitUseCase

    @Binds
    @Singleton
    abstract fun bindEditDeleteUseCase(
        useCase: EditDeleteUseCaseImpl
    ): EditDeleteUseCase
}
