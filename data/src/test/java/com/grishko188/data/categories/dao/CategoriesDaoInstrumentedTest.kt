package com.grishko188.data.categories.dao

import android.graphics.Color
import com.grishko188.data.features.categories.dao.CategoriesDao
import com.grishko188.data.features.categories.model.CategoryEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class CategoriesDaoInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var SUT: CategoriesDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_initial_categories_count_is_zero() = runTest {
        val count = SUT.count()
        assertEquals(0, count)
    }

    @Test
    fun test_insert_and_get_categories() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val count = SUT.count()
        assertEquals(testCategories.size, count)

        val categories = SUT.getCategories().first()

        assert(testCategories.all { test -> categories.find { actual -> actual.title == test.title } != null })
    }

    @Test
    fun test_insert_and_get_categories_by_ids() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val allCategories = SUT.getCategories().first()
        val expectedCategory = allCategories.last()
        val testCategoryId = expectedCategory.id
        val category = SUT.getCategory(testCategoryId)
        assertEquals(expectedCategory, category)
    }

    @Test
    fun test_update_category() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val allCategories = SUT.getCategories().first()
        assertEquals(testCategories.size, allCategories.size)

        val categoryToUpdate = allCategories[Random.nextInt(allCategories.size)]
        val testCategoryId = categoryToUpdate.id

        val expectedUpdatedCategory = categoryToUpdate.copy("Update Title").apply { id = categoryToUpdate.id }

        SUT.updateCategory(expectedUpdatedCategory)
        val actualCategory = SUT.getCategory(testCategoryId)

        assertEquals(expectedUpdatedCategory, actualCategory)
    }

    @Test
    fun test_get_categories_by_type() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val categoriesByType = SUT.getCategories("PERSONAL").first()
        val expectedCount = testCategories.count { it.type == "PERSONAL" }
        assertEquals(expectedCount, categoriesByType.size)
    }

    @Test
    fun test_get_categories_by_ids() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val allCategories = SUT.getCategories().first()

        val targetIds = allCategories.subList(0, 3).map { it.id }

        val categoriesByIds = SUT.getCategories(targetIds).first()

        assertEquals(targetIds.size, categoriesByIds.size)
        assert(categoriesByIds.all { entity -> targetIds.contains(entity.id) })
    }

    @Test
    fun test_insert_single_category() = runTest {
        SUT.deleteAll()
        val testCategories = testEntities()
        SUT.insertCategories(testCategories)

        val newCategory = CategoryEntity("New Category", Color.CYAN, "PERSONAL", isMutable = true)
        SUT.insertCategory(newCategory)

        val allCategories = SUT.getCategories().first()

        assertEquals(testCategories.size + 1, allCategories.size)
        assert(allCategories.find { entity -> entity.title == newCategory.title } != null)
    }


    @Test
    fun test_delete_categories() = runTest {
        SUT.deleteAll()
        var count = SUT.count()
        assertEquals(0, count)

        val testCategories = testEntities()
        SUT.insertCategories(testCategories)
        val categories = SUT.getCategories().first()
        assertEquals(testCategories.size, categories.size)

        val categoryToDelete = categories.first()
        SUT.deleteCategory(categoryToDelete.id)

        count = SUT.count()
        assertEquals(testCategories.size - 1, count)
    }

    private fun testEntities() = arrayListOf(
        CategoryEntity(title = "Title 1", color = Color.WHITE, type = "PERSONAL", isMutable = true),
        CategoryEntity(title = "Title 2", color = Color.BLACK, type = "PERSONAL", isMutable = true),
        CategoryEntity(title = "Title 3", color = Color.YELLOW, type = "PERSONAL", isMutable = true),
        CategoryEntity(title = "Title 3", color = Color.GREEN, type = "PERSONAL", isMutable = true),
        CategoryEntity(title = "Global 1", color = Color.RED, type = "GLOBAL", isMutable = false),
        CategoryEntity(title = "Global 2", color = Color.BLUE, type = "GLOBAL", isMutable = false),
        CategoryEntity(title = "Global 3", color = Color.GREEN, type = "GLOBAL", isMutable = false),
        CategoryEntity(title = "Global 4", color = Color.MAGENTA, type = "GLOBAL", isMutable = false),
    )
}