package com.chkan.bestpractices.simple_paging.di

import com.chkan.bestpractices.simple_paging.models.PassengersUIMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    internal fun provideMapperUI() = PassengersUIMapper()
}