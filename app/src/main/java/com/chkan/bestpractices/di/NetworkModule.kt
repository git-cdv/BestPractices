package com.chkan.bestpractices.di

import android.content.Context
import android.util.Log
import com.chkan.bestpractices.BuildConfig
import com.chkan.bestpractices.data.sources.network.MainService
import com.chkan.bestpractices.di.quialifiers.InterceptorLogTag
import com.chkan.bestpractices.utils.NETWORK
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Provides
    internal fun providesBaseUrl() : String = BuildConfig.API_BASE_URL

    @Provides
    @Singleton
    internal fun provideRetrofit(BASE_URL : String, okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    internal fun provideMainService(retrofit : Retrofit) : MainService = retrofit.create(MainService::class.java)

    @Singleton
    @Provides
    @InterceptorLogTag
    internal fun logTag(): String = NETWORK

    @Provides
    internal fun chuckInterceptor(@ApplicationContext context: Context): ChuckerInterceptor =
        ChuckerInterceptor(context)

    @Provides
    internal fun loggingInterceptor(@InterceptorLogTag logTag: String): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .setLevel(if (BuildConfig.isDebug) Level.BASIC else Level.NONE)
            .log(Log.INFO)
            .request(logTag)
            .response(logTag)
            .build()
    }

    @Provides
    internal fun okHttpClient(
        logging: LoggingInterceptor,
        chuck: ChuckerInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(chuck)
        return builder.build()
    }

}