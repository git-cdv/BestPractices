package com.chkan.bestpractices.alarm_manager

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {
    @Provides
    internal fun provideAlarmScheduler(@ApplicationContext context: Context): AndroidAlarmScheduler =
        AndroidAlarmScheduler(context)
}