package com.grishko188.data.features.categories.datasource

import com.grishko188.data.features.categories.model.CategoryDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface CategoriesDataSource {

    fun getCategories(): Flow<List<CategoryDataModel>>
    fun getCategories(type: String): Flow<List<CategoryDataModel>> = emptyFlow()
    suspend fun getCategory(id: Int): CategoryDataModel {
        return CategoryDataModel.EMPTY
    }

    suspend fun count(): Int = 0
    suspend fun addCategories(categoryDto: List<CategoryDataModel>) {}
    suspend fun addCategory(categoryDto: CategoryDataModel) {}
    suspend fun deleteCategory(categoryId: Int) {}
    suspend fun updateCategory(categoryDto: CategoryDataModel) {}
}