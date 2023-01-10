package com.grishko188.data.di

import com.google.gson.Gson
import com.grishko188.data.core.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module(includes = [InterceptorsModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonFactory.create()

    @Provides
    @Singleton
    fun converterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun okHttpClient(
        @CacheFolder cacheFolder: File,
        @Interceptors interceptors: List<Interceptor>,
        @NetworkInterceptors networkInterceptors: List<Interceptor>
    ): OkHttpClient = OkHttpClientFactory.create(
        cacheFolder,
        interceptors,
        networkInterceptors
    )

    @Provides
    @Singleton
    fun retrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        @ServerUrl baseUrl: String
    ): Retrofit = RetrofitFactory.create(okHttpClient, converterFactory, baseUrl)
}

@Module
@InstallIn(SingletonComponent::class)
object InterceptorsModule {
    @Provides
    @Interceptors
    @Singleton
    fun provideInterceptors(): List<@JvmWildcard Interceptor> = arrayListOf(HttpLoggingInterceptor())

    @Provides
    @NetworkInterceptors
    @Singleton
    fun provideNetworkInterceptors(): List<@JvmWildcard Interceptor> = emptyList()
}