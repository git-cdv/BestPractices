package com.chkan.bestpractices.di

import com.chkan.bestpractices.ui.dropdown_list.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author Dmytro Chkan on 05.09.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
interface DropDownModule {

    @Binds
    fun provideReadRawResource(readRawResourceImpl: ReadRawResource.Base): ReadRawResource

    @Binds
    fun provideCacheDataSource(cacheDataSourceImpl: CacheDataSource.Base): CacheDataSource

    @Binds
    fun provideDropDownMapper(dropDownMapperImpl: DropDownMapper.Base): DropDownMapper

    @Binds
    fun provideRepository(repositoryImpl: Repository.Base): Repository

    @Binds
    fun provideUiMapper(UiMapperImpl: UiMapper.Base): UiMapper

}