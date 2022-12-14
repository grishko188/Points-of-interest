package com.grishko188.data.di

import com.grishko188.data.core.ServerUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApiConstantsModule {

    private val isDebugMode = true

    @Provides
    @ServerUrl
    fun provideServerUrl() = when (isDebugMode) {
        true -> SERVER_URL_TEST
        else -> SERVER_URL_PROD
    }

    private companion object {
        const val SERVER_URL_PROD = "https://www.google.com/"
        const val SERVER_URL_TEST = "https://www.google.com/"
    }
}