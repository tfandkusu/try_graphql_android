package com.tfandkusu.graphql.di

import com.tfandkusu.graphql.usecase.edit.EditOnCreateUseCase
import com.tfandkusu.graphql.usecase.edit.EditOnCreateUseCaseImpl
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
        useCase: EditOnCreateUseCaseImpl
    ): EditOnCreateUseCase

    @Binds
    @Singleton
    abstract fun bindEditSubmitUseCase(
        useCase: EditSubmitUseCaseImpl
    ): EditSubmitUseCase
}
