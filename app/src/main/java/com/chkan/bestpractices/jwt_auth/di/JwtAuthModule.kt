package com.chkan.bestpractices.jwt_auth.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.chkan.bestpractices.jwt_auth.data.auth.api.AuthApi
import com.chkan.bestpractices.jwt_auth.data.auth.JwtAuthRepository
import com.chkan.bestpractices.jwt_auth.data.auth.JwtAuthRepositoryImpl
import com.chkan.bestpractices.jwt_auth.data.refresh_token.TokenAuthenticator
import com.chkan.bestpractices.jwt_auth.data.utils.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JwtAuthModule {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Singleton
    @Provides
    @Named("Jwt")
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: TokenAuthenticator,
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(prefs: SharedPreferences): AuthInterceptor =
        AuthInterceptor(prefs)



    @Provides
    @Singleton
    fun provideAuthApi(@Named("Jwt") okHttpClient: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") //http://10.0.2.2:8080/  for emulator
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, prefs: SharedPreferences): JwtAuthRepository {
        return JwtAuthRepositoryImpl(api, prefs)
    }
}