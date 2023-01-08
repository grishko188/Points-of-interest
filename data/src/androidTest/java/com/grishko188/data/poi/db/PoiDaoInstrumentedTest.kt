package com.grishko188.data.poi.db

import android.graphics.Color
import com.grishko188.data.core.Local
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.poi.db.PoiDao
import com.grishko188.data.features.poi.model.OrderByColumns
import com.grishko188.data.features.poi.model.PoiCommentEntity
import com.grishko188.data.features.poi.model.PoiEntity
import com.grishko188.data.features.poi.model.PoiWithCategoriesEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.NullPointerException
import java.util.*
import javax.inject.Inject
import kotlin.test.*
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class PoiDaoInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var categoriesDataSource: CategoriesDataSource

    @Inject
    lateinit var SUT: PoiDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_poi_table_is_empty() = runTest {
        val count = SUT.getPoiList(OrderByColumns.DATE.columnName).first().size
        assertEquals(0, count)
    }

    @Test
    fun test_insert_poi_transaction_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        assertNotNull(id)
    }

    @Test
    fun test_insert_and_get_poi_by_id_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        val poi = SUT.getPoi(id.toInt())
        assertEquals(id.toInt(), poi.entity.id)
        assertEquals(singlePoi.second.size, poi.categories.size)
        assert(poi.categories.all { category -> singlePoi.second.find { it == category.id } != null })
    }

    @Test
    fun test_update_poi_viewed_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        val poi = SUT.getPoi(id.toInt())
        assertFalse(poi.entity.viewed)
        SUT.updatePoiViewed(id.toInt(), true)
        val updatedPoi = SUT.getPoi(id.toInt())
        assertTrue(updatedPoi.entity.viewed)
    }

    @Test
    fun test_delete_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        val poi = SUT.getPoi(id.toInt())
        assertEquals(id.toInt(), poi.entity.id)

        SUT.deletePoi(id.toInt())

        val expected = try {
            SUT.getPoi(id.toInt())
        } catch (e: NullPointerException) {
            null
        }
        assertNull(expected)
    }

    @Test
    fun test_get_used_categories() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val expectedCategoriesSet = TreeSet<Int>()
        testPoi.forEach {
            SUT.insertPoiTransaction(it.first, it.second)
            expectedCategoriesSet.addAll(it.second)
        }
        val usedCategories = SUT.getUsedCategoriesIds().first()
        usedCategories.sorted()

        Assert.assertArrayEquals(expectedCategoriesSet.toTypedArray(), usedCategories.toTypedArray())
    }

    @Test
    fun test_get_poi_list_ordered_by_date_DESC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testPoi.forEach {
            SUT.insertPoiTransaction(it.first, it.second)
        }
        val poiList = SUT.getPoiList(OrderByColumns.DATE.columnName).first()
        assertEquals(testPoi.size, poiList.size)

        val sortedCopy = arrayListOf<PoiWithCategoriesEntity>()
        sortedCopy.addAll(poiList)
        sortedCopy.sortByDescending { it.entity.creationDateTime }

        Assert.assertArrayEquals(sortedCopy.toTypedArray(), poiList.toTypedArray())
    }

    @Test
    fun test_get_poi_list_ordered_by_severity_ASC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testPoi.forEach {
            SUT.insertPoiTransaction(it.first, it.second)
        }
        val poiList = SUT.getPoiList(OrderByColumns.SEVERITY.columnName).first()
        assertEquals(testPoi.size, poiList.size)

        val sortedCopy = arrayListOf<PoiWithCategoriesEntity>()
        sortedCopy.addAll(poiList)
        sortedCopy.sortBy { it.entity.severity }

        Assert.assertArrayEquals(sortedCopy.toTypedArray(), poiList.toTypedArray())
    }

    @Test
    fun test_get_poi_list_ordered_by_title_ASC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testPoi.forEach {
            SUT.insertPoiTransaction(it.first, it.second)
        }
        val poiList = SUT.getPoiList(OrderByColumns.TITLE.columnName).first()
        assertEquals(testPoi.size, poiList.size)

        val sortedCopy = arrayListOf<PoiWithCategoriesEntity>()
        sortedCopy.addAll(poiList)
        sortedCopy.sortBy { it.entity.title }

        Assert.assertArrayEquals(sortedCopy.toTypedArray(), poiList.toTypedArray())
    }

    @Test
    fun test_search_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testPoi.forEach {
            SUT.insertPoiTransaction(it.first, it.second)
        }

        val query1 = "Title"
        val result1 = SUT.searchPoi(query1)
        assertEquals(4, result1.size)
        assert(result1.all { it.entity.title.contains(query1, ignoreCase = true) })

        val query2 = "poi"
        val result2 = SUT.searchPoi(query2)
        assertEquals(1, result2.size)
        assert(result2.first().entity.body.contains(query2))

        val query3 = "android.com"
        val result3 = SUT.searchPoi(query3)
        assertEquals(1, result3.size)
        assert(result3.first().entity.contentLink.contains(query3))

        val query4 = "des"
        val result4 = SUT.searchPoi("*$query4*")
        assertEquals(2, result4.size)
        assert(result4.all { it.entity.body.contains(query4, ignoreCase = true) })

        val query5 = "daskaskjfaksjfkasjfkasfaes"
        val result5 = SUT.searchPoi("*$query5*")
        assertTrue(result5.isEmpty())
    }

    @Test
    fun test_add_comment_updates_poi_comment_count_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        var poi = SUT.getPoi(id.toInt())
        assertEquals(0, poi.entity.commentsCount)

        val commentEntity = PoiCommentEntity(id.toInt(), "Test Test", creationDataTime = Clock.System.now())

        SUT.insertCommentTransaction(commentEntity)

        poi = SUT.getPoi(id.toInt())
        assertEquals(1, poi.entity.commentsCount)

        val commentEntity2 = PoiCommentEntity(id.toInt(), "Test Test 2", creationDataTime = Clock.System.now())

        SUT.insertCommentTransaction(commentEntity2)

        poi = SUT.getPoi(id.toInt())
        assertEquals(2, poi.entity.commentsCount)
    }

    @Test
    fun test_delete_comment_transaction_update_poi_comments_count_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)
        var poi = SUT.getPoi(id.toInt())
        assertEquals(0, poi.entity.commentsCount)

        val commentEntity = PoiCommentEntity(id.toInt(), "Test Test", creationDataTime = Clock.System.now())

        val idToRemove = SUT.insertCommentTransaction(commentEntity)

        poi = SUT.getPoi(id.toInt())
        assertEquals(1, poi.entity.commentsCount)

        SUT.deleteCommentTransaction(idToRemove.toInt())

        poi = SUT.getPoi(id.toInt())
        assertEquals(0, poi.entity.commentsCount)
    }

    @Test
    fun test_get_all_comments_for_poi_sorted_by_creation_time_DESC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoiTransaction(singlePoi.first, singlePoi.second)

        val comments = arrayListOf(
            PoiCommentEntity(id.toInt(), "Test Test", creationDataTime = Clock.System.now()),
            PoiCommentEntity(id.toInt(), "Some Test Test", creationDataTime = Clock.System.now()),
            PoiCommentEntity(id.toInt(), "Test Test 2", creationDataTime = Clock.System.now())
        )
        comments.forEach { commentEntity ->
            SUT.insertCommentTransaction(commentEntity)
        }
        val actual = SUT.getComments(id.toInt()).first()
        assertEquals(comments.size, actual.size)

        val sortedCopy = arrayListOf<PoiCommentEntity>()
        sortedCopy.addAll(actual)
        sortedCopy.sortByDescending { it.creationDataTime }

        Assert.assertArrayEquals(sortedCopy.toTypedArray(), actual.toTypedArray())
    }

    private val singlePoi = PoiEntity(
        title = "Title",
        body = "Body",
        imageUrl = "https://www.google.com/image",
        contentLink = "https://www.google.com/somethingelse?query=1",
        creationDateTime = Clock.System.now(),
        commentsCount = 0,
        severity = 0,
        viewed = false
    ) to arrayListOf(1, 2)

    private val testPoi = arrayListOf(
        PoiEntity(
            title = "Title 1",
            body = "Description",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDateTime = Clock.System.now(),
            commentsCount = 0,
            severity = 0,
            viewed = false
        ) to arrayListOf(1, 5),

        PoiEntity(
            title = "Random Title 1",
            body = "Some description",
            imageUrl = "https://www.someweb.com/image",
            contentLink = "https://www.someweb.com/somethingelse?query=1",
            creationDateTime = Clock.System.now() - 2.days,
            commentsCount = 0,
            severity = 1,
            viewed = false
        ) to arrayListOf(2, 6),

        PoiEntity(
            title = "This is another title",
            body = "Test body for poi",
            imageUrl = "https://www.android.com/image",
            contentLink = "https://www.android.com/somethingelse?query=1",
            creationDateTime = Clock.System.now() - 5.days,
            commentsCount = 0,
            severity = 2,
            viewed = false
        ) to arrayListOf(3, 7),

        PoiEntity(
            title = "Random Title 1",
            body = "Well here is something else",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDateTime = Clock.System.now() - 90.days,
            commentsCount = 0,
            severity = 3,
            viewed = false,
        ) to arrayListOf(4, 5)
    )

    private val testCategories = arrayListOf(
        CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
        CategoryDataModel(2, "Medium", Color.RED, type = "SEVERITY", false),
        CategoryDataModel(3, "Normal", Color.RED, type = "SEVERITY", false),
        CategoryDataModel(4, "Low", Color.RED, type = "SEVERITY", false),
        CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true),
        CategoryDataModel(6, "Name 2", Color.WHITE, type = "PERSONAL", true),
        CategoryDataModel(7, "Name 3", Color.WHITE, type = "PERSONAL", true)
    )
}