package com.grishko188.data.di

import android.content.Context
import androidx.room.Room
import com.grishko188.data.database.PoiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoiDatabase =
        Room.databaseBuilder(
            context,
            PoiDatabase::class.java,
            "grishko188-poi-database"
        ).build()
}