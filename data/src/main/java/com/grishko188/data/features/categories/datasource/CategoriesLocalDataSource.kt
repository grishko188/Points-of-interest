package com.grishko188.data.features.categories.datasource

import com.grishko188.data.features.categories.dao.CategoriesDao
import com.grishko188.data.features.categories.model.CategoryDto
import com.grishko188.data.features.categories.model.CategoryEntity
import com.grishko188.data.features.categories.model.toDto
import com.grishko188.data.features.categories.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesLocalDataSource @Inject constructor(
    private val dao: CategoriesDao
) : CategoriesDataSource {

    override fun getCategories(): Flow<List<CategoryDto>> =
        dao.getCategories().mapToDto()

    override fun getCategories(type: String): Flow<List<CategoryDto>> =
        dao.getCategories(type).mapToDto()

    override fun getCategory(id: Int): Flow<CategoryDto> =
        dao.getCategory(id).map { it.toDto() }

    override suspend fun count(): Int = dao.count()

    override suspend fun addCategories(categoryDto: List<CategoryDto>) {
        dao.insertCategories(categoryDto.map { it.toEntity() })
    }

    override suspend fun addCategory(categoryDto: CategoryDto) {
        dao.insertCategory(categoryDto.toEntity())
    }

    override suspend fun deleteCategory(categoryId: Int) {
        dao.deleteCategory(categoryId)
    }

    override suspend fun updateCategory(categoryDto: CategoryDto) {
        dao.updateCategory(categoryDto.toEntity())
    }

    private fun Flow<List<CategoryEntity>>.mapToDto() = this.map { list -> list.map { it.toDto() } }
}