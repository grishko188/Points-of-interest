package com.grishko188.data.features.categories.datasource

import com.grishko188.data.features.categories.dao.CategoriesDao
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.categories.model.CategoryEntity
import com.grishko188.data.features.categories.model.toDataModel
import com.grishko188.data.features.categories.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesLocalDataSource @Inject constructor(
    private val dao: CategoriesDao
) : CategoriesDataSource {

    override fun getCategories(): Flow<List<CategoryDataModel>> =
        dao.getCategories().mapToDataModels()

    override fun getCategories(type: String): Flow<List<CategoryDataModel>> =
        dao.getCategories(type).mapToDataModels()

    override fun getCategories(ids: List<Int>): Flow<List<CategoryDataModel>> =
        dao.getCategories(ids).mapToDataModels()

    override suspend fun getCategory(id: Int): CategoryDataModel =
        dao.getCategory(id).toDataModel()

    override suspend fun count(): Int = dao.count()

    override suspend fun addCategories(models: List<CategoryDataModel>) {
        dao.insertCategories(models.map { it.toEntity() })
    }

    override suspend fun addCategory(model: CategoryDataModel) {
        dao.insertCategory(model.toEntity())
    }

    override suspend fun deleteCategory(categoryId: Int) {
        dao.deleteCategory(categoryId)
    }

    override suspend fun updateCategory(model: CategoryDataModel) {
        dao.updateCategory(model.toEntity())
    }

    private fun Flow<List<CategoryEntity>>.mapToDataModels() = this.map { list -> list.map { it.toDataModel() } }
}