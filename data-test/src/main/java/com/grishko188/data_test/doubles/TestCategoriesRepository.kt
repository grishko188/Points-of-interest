package com.grishko188.data_test.doubles

import android.graphics.Color
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.categories.models.CreateCategoryPayload
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

val testCategories = listOf(
    Category("1", "Title", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true),
    Category("2", "Title 2", Color.BLACK, categoryType = CategoryType.PERSONAL, isMutable = true),
    Category("3", "Title 3", Color.RED, categoryType = CategoryType.PERSONAL, isMutable = true),
    Category("4", "Title 4", Color.GREEN, categoryType = CategoryType.PERSONAL, isMutable = true)
)

class TestCategoriesRepository @Inject constructor() : CategoriesRepository {

    private val categoriesState = MutableStateFlow(testCategories)

    override suspend fun sync() {}

    override fun getCategories(): Flow<List<Category>> = categoriesState

    override fun getCategories(ids: List<Int>): Flow<List<Category>> =
        categoriesState.map { it.filter { category -> category.id.toInt() in ids } }

    override fun getCategories(type: CategoryType): Flow<List<Category>> =
        categoriesState.map { it.filter { category -> category.categoryType == type } }

    override suspend fun getCategory(id: String): Category {
        return categoriesState.value.find { it.id == id }!!
    }

    override suspend fun addCategory(payload: CreateCategoryPayload) {
        val newCategory = Category(
            id = Random.nextInt().toString(),
            title = payload.title,
            color = payload.color,
            categoryType = CategoryType.PERSONAL,
            isMutable = true
        )
        val updatedCategories = categoriesState.value.toMutableList()
        updatedCategories.add(newCategory)
        categoriesState.tryEmit(updatedCategories)
    }

    override suspend fun updateCategory(category: Category) {
        val updatedCategories = categoriesState.value.toMutableList()
        updatedCategories.removeIf { it.id == category.id }
        updatedCategories.add(category)
        categoriesState.tryEmit(updatedCategories)
    }

    override suspend fun deleteCategory(categoryId: String) {
        val updatedCategories = categoriesState.value.toMutableList()
        updatedCategories.removeIf { it.id == categoryId}
        categoriesState.tryEmit(updatedCategories)
    }
}