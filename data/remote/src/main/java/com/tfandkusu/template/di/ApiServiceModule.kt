package com.tfandkusu.template.di

import com.tfandkusu.template.api.TemplateApiServiceBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun providesTemplateApiService() = TemplateApiServiceBuilder.build()
}
