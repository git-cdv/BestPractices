package com.chkan.bestpractices.coroutines.di

import com.chkan.bestpractices.coroutines.data.UserRepositoryImpl
import com.chkan.bestpractices.coroutines.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideUserRepoImpl(repository: UserRepositoryImpl): UserRepository
}