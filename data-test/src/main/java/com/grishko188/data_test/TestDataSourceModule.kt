package com.grishko188.data_test

import com.grishko188.data.core.Local
import com.grishko188.data.core.Remote
import com.grishko188.data.di.DataSourceModule
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.datasource.CategoriesFakeRemoteDataSource
import com.grishko188.data.features.categories.datasource.CategoriesLocalDataSource
import com.grishko188.data.features.poi.datasource.*
import com.grishko188.data.features.profile.datasource.ProfileDataSource
import com.grishko188.data.features.profile.datasource.ProfileLocalDataSource
import com.grishko188.data_test.doubles.TestLocalImageDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
interface TestDataSourceModule {

    @Binds
    @Local
    fun bindImageDataSource(impl: TestLocalImageDataSource): ImageDataSource

    @Binds
    @Local
    fun bindCategoryLocalDataSource(dataSource: CategoriesLocalDataSource): CategoriesDataSource

    @Binds
    @Remote
    fun bindCategoryRemoteDataSource(dataSource: CategoriesFakeRemoteDataSource): CategoriesDataSource

    @Binds
    @Local
    fun bindProfileLocalDataSource(dataSource: ProfileLocalDataSource): ProfileDataSource

    @Binds
    @Local
    fun bindPoiLocalDataSource(dataSource: PoiLocalDataSource): PoiDataSource

    @Binds
    @Remote
    fun bindWizardRemoteDataSource(dataSource: WizardRemoteDataSource): WizardDataSource
}