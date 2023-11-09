package com.chkan.bestpractices.jwt_auth.di

import android.content.SharedPreferences
import com.chkan.bestpractices.jwt_auth.data.refresh_token.RefreshTokenApi
import com.chkan.bestpractices.jwt_auth.data.refresh_token.TokenAuthenticator
import com.chkan.bestpractices.jwt_auth.data.refresh_token.TokenRepository
import com.chkan.bestpractices.jwt_auth.data.refresh_token.TokenRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RefreshTokenModule {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Singleton
    @Provides
    fun provideTokenAuthenticator(repo: TokenRepository): TokenAuthenticator =
        TokenAuthenticator(repo)

    @Provides
    @Singleton
    fun provideRefreshTokenApi(): RefreshTokenApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") //http://10.0.2.2:8080/  for emulator
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTokenRepository(api: RefreshTokenApi, prefs: SharedPreferences): TokenRepository {
        return TokenRepositoryImpl(api, prefs)
    }

}