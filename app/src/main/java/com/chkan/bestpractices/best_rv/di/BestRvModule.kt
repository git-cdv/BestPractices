package com.chkan.bestpractices.best_rv.di

import com.chkan.bestpractices.best_rv.data.UsersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object BestRvModule {

    @Provides
    internal fun provideUsersService() : UsersService = UsersService()
}