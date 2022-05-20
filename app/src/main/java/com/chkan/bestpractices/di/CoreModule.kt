package com.chkan.bestpractices.di

import com.chkan.bestpractices.core.Dispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Dmytro Chkan on 20.05.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    internal fun provideDispatchers() : Dispatchers = Dispatchers.Base()
}