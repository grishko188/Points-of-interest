package com.grishko188.data.categories.datasource

import android.graphics.Color
import com.grishko188.data.core.Local
import com.grishko188.data.core.Remote
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class CategoriesLocalDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var SUT: CategoriesDataSource

    @Inject
    @Remote
    lateinit var remoteDataSource: CategoriesDataSource

    private lateinit var dispatcher: TestDispatcher

    @Before
    fun init() {
        hiltRule.inject()
        dispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun test_addCategories_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()
        SUT.addCategories(initialCategories)
        val localCategories = SUT.getCategories().first()

        assertEquals(initialCategories.size, localCategories.size)
        assert(localCategories.all { local -> initialCategories.find { remote -> remote.title == local.title && remote.color == local.color } != null })
    }

    @Test
    fun test_addCategory_invoke_flow_update_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        var local = SUT.getCategories().first()
        assertEquals(initialCategories.size, local.size)

        SUT.addCategory(
            CategoryDataModel(
                id = UNSPECIFIED_ID,
                title = "Title 1",
                color = Color.WHITE,
                type = "PERSONAL",
                isMutable = true
            )
        )
        local = SUT.getCategories().first()
        assertEquals(initialCategories.size + 1, local.size)
        assert(local.find { it.title == "Title 1" } != null)
    }

    @Test
    fun test_getCategory_by_id_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        val local = SUT.getCategories().first()
        val expected = local[Random.nextInt(local.size)]

        val actual = SUT.getCategory(expected.id)
        assertEquals(expected, actual)
    }

    @Test(expected = java.lang.NullPointerException::class)
    fun test_getCategory_by_id_with_not_existent_id_fails_with_NPE() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()
        SUT.addCategories(initialCategories)
        SUT.getCategory(Int.MAX_VALUE)
    }

    @Test
    fun test_getCategories_by_ids_list_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        val local = SUT.getCategories().first()
        val expected1 = local[Random.nextInt(local.size)]
        val expected2 = local[Random.nextInt(local.size)]
        val expected3 = local[Random.nextInt(local.size)]

        val expectedResult = setOf(expected1, expected2, expected3).sortedBy { it.id }

        val actual = SUT.getCategories(expectedResult.map { it.id }).first()
        Assert.assertArrayEquals(expectedResult.toTypedArray(), actual.sortedBy { it.id }.toTypedArray())
    }

    @Test
    fun test_getCategories_by_type_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        SUT.addCategories(testDataModels())

        val actual = SUT.getCategories("PERSONAL").first()
        assertEquals(testDataModels().size, actual.size)
    }

    @Test
    fun test_deleteCategory_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        val local = SUT.getCategories().first()
        val expected = local[Random.nextInt(local.size)]

        SUT.deleteCategory(expected.id)
        val result = SUT.getCategories().first()

        assertEquals(initialCategories.size - 1, result.size)
        assert(result.none { it.id == expected.id })
    }

    @Test
    fun test_deleteCategory_with_non_existent_id_not_crashing() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()
        SUT.addCategories(initialCategories)
        SUT.deleteCategory(Int.MAX_VALUE)
    }

    @Test
    fun test_updateCategory_success() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()

        SUT.addCategories(initialCategories)
        val local = SUT.getCategories().first()

        val expected = local[Random.nextInt(local.size)]
        val updated = expected.copy(title = "Title Updated")

        SUT.updateCategory(updated)
        val result = SUT.getCategory(expected.id)

        assertEquals(updated, result)
    }

    @Test
    fun test_updateCategory_with_non_existent_id_not_crashing_and_not_creating_broken_items() = runTest {
        val initialCategories = remoteDataSource.getCategories().first()
        SUT.addCategories(initialCategories)
        val local = SUT.getCategories().first()

        val expected = local[Random.nextInt(local.size)]
        val updated = expected.copy(title = "Title Updated", id = Int.MAX_VALUE)

        SUT.updateCategory(updated)
        val result = SUT.getCategory(expected.id)

        assertNotEquals(updated, result)

        val failedResult = try {
            SUT.getCategory(Int.MAX_VALUE)
        } catch (e: java.lang.NullPointerException) {
            null
        }
        assertNull(failedResult)
    }

    private fun testDataModels() = arrayListOf(
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 1", color = Color.WHITE, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 2", color = Color.BLACK, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 3", color = Color.YELLOW, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 3", color = Color.GREEN, type = "PERSONAL", isMutable = true)
    )
}