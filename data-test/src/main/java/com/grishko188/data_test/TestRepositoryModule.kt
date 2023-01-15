package com.grishko188.data_test

import com.grishko188.data.di.RepoModule
import com.grishko188.data_test.doubles.TestCategoriesRepository
import com.grishko188.data_test.doubles.TestPoiRepository
import com.grishko188.data_test.doubles.TestProfileRepository
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import com.grishko188.domain.features.poi.repo.PoiRepository
import com.grishko188.domain.features.profile.repo.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
interface TestRepositoryModule {

    @Binds
    @Singleton
    fun bindPoiRepository(impl: TestPoiRepository): PoiRepository

    @Binds
    @Singleton
    fun bindCategoriesRepository(impl: TestCategoriesRepository): CategoriesRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: TestProfileRepository): ProfileRepository
}