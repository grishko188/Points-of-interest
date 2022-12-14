package com.grishko188.data.features.categories.repository

import com.grishko188.data.core.Local
import com.grishko188.data.core.Remote
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDto
import com.grishko188.data.features.categories.model.toDomain
import com.grishko188.data.features.categories.model.toDto
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.categories.models.CreateCategoryPayload
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    @Remote private val remoteDataSource: CategoriesDataSource,
    @Local private val localDataSource: CategoriesDataSource
) : CategoriesRepository {

    override suspend fun sync() {
        if (localDataSource.count() == 0) {
            remoteDataSource.getCategories().collect {
                localDataSource.addCategories(it)
            }
        }
    }

    override fun getCategories(): Flow<List<Category>> =
        localDataSource.getCategories().mapToDomain()

    override fun getCategories(type: CategoryType): Flow<List<Category>> =
        localDataSource.getCategories(type.name).mapToDomain()

    override fun getCategory(id: String): Flow<Category> =
        localDataSource.getCategory(id.toInt()).map { it.toDomain() }

    override suspend fun addCategory(payload: CreateCategoryPayload) {
        localDataSource.addCategory(payload.toDto())
    }

    override suspend fun updateCategory(category: Category) {
        localDataSource.updateCategory(category.toDto())
    }

    override suspend fun deleteCategory(categoryId: String) {
        localDataSource.deleteCategory(categoryId.toInt())
    }

    private fun Flow<List<CategoryDto>>.mapToDomain() =
        this.map { list -> list.map { it.toDomain() } }
}