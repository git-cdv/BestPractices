package com.chkan.bestpractices.di

import android.content.Context
import android.util.Log
import com.chkan.bestpractices.BuildConfig
import com.chkan.bestpractices.simple_paging.data.repo.PassengersRepoImpl
import com.chkan.bestpractices.simple_paging.data.sources.network.NetworkPassengersSource
import com.chkan.bestpractices.simple_paging.di.quialifiers.InterceptorLogTag
import com.chkan.bestpractices.simple_paging.domain.repo.PassengersRepo
import com.chkan.bestpractices.utils.NETWORK
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
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
    internal fun provideMainService(retrofit : Retrofit) : NetworkPassengersSource = retrofit.create(
        NetworkPassengersSource::class.java)

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
    fun provideRequestInterceptor() = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        builder.header("app-id", "62a891b745658d75b2f2f139")
        return@Interceptor chain.proceed(builder.build())
    }

    @Provides
    internal fun okHttpClient(
        logging: LoggingInterceptor,
        chuck: ChuckerInterceptor,
        requestInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(chuck)
            .addInterceptor(requestInterceptor)
        return builder.build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {
    @Binds
    fun providePassengersRepoImpl(repository: PassengersRepoImpl): PassengersRepo
}