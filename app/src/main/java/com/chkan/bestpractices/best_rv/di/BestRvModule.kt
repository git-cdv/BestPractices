package com.chkan.bestpractices.best_rv.di

import com.chkan.bestpractices.best_rv.data.UsersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BestRvModule {

    @Provides
    @Singleton
    internal fun provideUsersService() : UsersService = UsersService()
}