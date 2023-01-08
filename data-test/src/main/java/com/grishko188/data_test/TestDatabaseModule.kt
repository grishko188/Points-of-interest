package com.grishko188.data_test

import android.content.Context
import androidx.room.Room
import com.grishko188.data.database.PoiDatabase
import com.grishko188.data.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoiDatabase =
        Room.inMemoryDatabaseBuilder(
            context,
            PoiDatabase::class.java
        ).build()
}