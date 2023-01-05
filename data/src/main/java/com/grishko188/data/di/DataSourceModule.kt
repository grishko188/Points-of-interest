package com.grishko188.data.di

import com.grishko188.data.core.Local
import com.grishko188.data.core.Remote
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.datasource.CategoriesFakeRemoteDataSource
import com.grishko188.data.features.categories.datasource.CategoriesLocalDataSource
import com.grishko188.data.features.poi.datasource.*
import com.grishko188.data.features.profile.datasource.ProfileDataSource
import com.grishko188.data.features.profile.datasource.ProfileLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

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
    @Local
    fun bindLocalImageDataSource(dataSource: LocalImageDataSource): ImageDataSource

    @Binds
    @Remote
    fun bindWizardRemoteDataSource(dataSource: WizardRemoteDataSource): WizardDataSource
}