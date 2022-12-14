package com.grishko188.data.features.categories.datasource

import com.grishko188.data.features.categories.model.CategoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface CategoriesDataSource {

    fun getCategories(): Flow<List<CategoryDto>>
    fun getCategories(type: String): Flow<List<CategoryDto>> = emptyFlow()
    fun getCategory(id: Int): Flow<CategoryDto> = emptyFlow()

    suspend fun count(): Int = 0
    suspend fun addCategories(categoryDto: List<CategoryDto>) {}
    suspend fun addCategory(categoryDto: CategoryDto) {}
    suspend fun deleteCategory(categoryId: Int) {}
    suspend fun updateCategory(categoryDto: CategoryDto) {}
}