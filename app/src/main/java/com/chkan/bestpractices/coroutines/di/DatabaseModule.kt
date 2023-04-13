package com.chkan.bestpractices.coroutines.di

import android.content.Context
import androidx.room.Room
import com.chkan.bestpractices.coroutines.data.room.UserDao
import com.chkan.bestpractices.coroutines.data.room.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room
            .databaseBuilder(
                appContext,
                UserDatabase::class.java,
                "users_db")
            .build()
    }

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao {
        return db.userDao
    }

}