package com.grishko188.data.di

import android.content.Context
import com.grishko188.data.core.CacheFolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheFolderModule {

    @Provides
    @CacheFolder
    @Singleton
    fun provideCacheFolder(@ApplicationContext context: Context): File = context.cacheDir
}