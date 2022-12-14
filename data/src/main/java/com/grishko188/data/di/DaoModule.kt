package com.grishko188.data.di

import com.grishko188.data.database.PoiDatabase
import com.grishko188.data.features.categories.dao.CategoriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCategoriesDao(database: PoiDatabase): CategoriesDao = database.categoriesDao()
}