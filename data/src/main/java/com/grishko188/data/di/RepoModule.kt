package com.grishko188.data.di

import com.grishko188.data.features.categories.repository.CategoriesRepositoryImpl
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    @Singleton
    fun bindCategoriesRepo(impl: CategoriesRepositoryImpl): CategoriesRepository
}