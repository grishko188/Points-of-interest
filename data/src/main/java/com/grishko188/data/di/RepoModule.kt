package com.grishko188.data.di

import com.grishko188.data.features.categories.repository.CategoriesRepositoryImpl
import com.grishko188.data.features.poi.repository.PoiRepositoryImpl
import com.grishko188.data.features.profile.repository.ProfileRepositoryImpl
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import com.grishko188.domain.features.poi.repo.PoiRepository
import com.grishko188.domain.features.profile.repo.ProfileRepository
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

    @Binds
    @Singleton
    fun bindProfileRepo(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindPoiRepo(impl: PoiRepositoryImpl): PoiRepository
}