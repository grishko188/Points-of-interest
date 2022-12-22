package com.grishko188.data.features.categories.datasource

import com.grishko188.data.features.categories.model.CategoryDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface CategoriesDataSource {

    fun getCategories(): Flow<List<CategoryDataModel>>
    fun getCategories(type: String): Flow<List<CategoryDataModel>> = emptyFlow()
    fun getCategories(ids: List<Int>):Flow<List<CategoryDataModel>> = emptyFlow()
    suspend fun getCategory(id: Int): CategoryDataModel {
        return CategoryDataModel.EMPTY
    }

    suspend fun count(): Int = 0
    suspend fun addCategories(models: List<CategoryDataModel>) {}
    suspend fun addCategory(model: CategoryDataModel) {}
    suspend fun deleteCategory(categoryId: Int) {}
    suspend fun updateCategory(model: CategoryDataModel) {}
}