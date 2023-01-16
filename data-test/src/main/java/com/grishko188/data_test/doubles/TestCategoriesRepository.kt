package com.grishko188.data_test.doubles

import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.categories.models.CreateCategoryPayload
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TestCategoriesRepository @Inject constructor() : CategoriesRepository {
    override suspend fun sync() {}

    override fun getCategories(): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    override fun getCategories(ids: List<Int>): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    override fun getCategories(type: CategoryType): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategory(id: String): Category {
        TODO("Not yet implemented")
    }

    override suspend fun addCategory(payload: CreateCategoryPayload) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(categoryId: String) {
        TODO("Not yet implemented")
    }
}