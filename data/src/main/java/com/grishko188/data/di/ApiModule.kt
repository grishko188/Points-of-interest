package com.grishko188.data.di

import com.grishko188.data.features.poi.api.WizardServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideWizardApi(retrofit: Retrofit): WizardServiceApi =
        retrofit.create(WizardServiceApi::class.java)
}