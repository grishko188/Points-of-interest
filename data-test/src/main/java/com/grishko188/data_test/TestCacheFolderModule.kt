package com.grishko188.data_test

import com.grishko188.data.core.CacheFolder
import com.grishko188.data.di.CacheFolderModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.junit.rules.TemporaryFolder
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CacheFolderModule::class]
)
object TestCacheFolderModule {

    @Provides
    fun provideTemporaryFolder(): TemporaryFolder {
        val tempFolder = TemporaryFolder.builder().assureDeletion().build()
        tempFolder.create()
        return tempFolder
    }

    @Provides
    @CacheFolder
    @Singleton
    fun provideCacheFolder(tempFolder: TemporaryFolder): File =
        tempFolder.newFolder("test/cachedir")
}