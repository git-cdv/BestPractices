package com.chkan.bestpractices.auth.di

import android.content.Context
import com.chkan.bestpractices.auth.data.TokenStorage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.chkan.bestpractices.auth.data.github.GithubApi
import com.chkan.bestpractices.auth.data.network.AuthorizationInterceptor
import com.ihsanbal.logging.LoggingInterceptor
import com.chkan.bestpractices.auth.data.network.AuthorizationFailedInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthorizationService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author Dmytro Chkan on 09.09.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    @Named("Auth")
    internal fun provideAuthRetrofit(@Named("Auth") okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    internal fun provideAuthService(@Named("Auth") retrofit : Retrofit) : GithubApi = retrofit.create(
        GithubApi::class.java)

    @Provides
    @Named("Auth")
    internal fun okHttpClient(
        logging: LoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addNetworkInterceptor(AuthorizationInterceptor())
            .addNetworkInterceptor(AuthorizationFailedInterceptor(AuthorizationService(context), TokenStorage))
        return builder.build()
    }


}