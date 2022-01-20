package com.tfandkusu.graphql.di

import android.content.Context
import com.tfandkusu.graphql.api.ApolloClientBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    @Singleton
    fun providerApolloClient(
        @ApplicationContext context: Context
    ) = ApolloClientBuilder.build(context)
}
