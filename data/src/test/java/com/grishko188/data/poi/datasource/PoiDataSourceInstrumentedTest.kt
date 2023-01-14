package com.grishko188.data.poi.datasource

import android.graphics.Color
import com.grishko188.data.core.Local
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.poi.datasource.PoiDataSource
import com.grishko188.data.features.poi.model.OrderByColumns
import com.grishko188.data.features.poi.model.PoiCommentDataModel
import com.grishko188.data.features.poi.model.PoiDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class PoiDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var SUT: PoiDataSource

    @Inject
    @Local
    lateinit var categoriesDataSource: CategoriesDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_create_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val expectedId = SUT.insertPoi(testCreationPoi)
        assertNotNull(expectedId)
        assertNotEquals(UNSPECIFIED_ID, expectedId.toInt())
    }

    @Test
    fun test_create_poi_updates_used_categories_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        SUT.insertPoi(testCreationPoi)
        val expectedCategories = testCreationPoi.categories.map { it.id }.toSet().sorted()
        val usedCategories = SUT.getUsedCategoriesIds().first().sorted()
        Assert.assertArrayEquals(expectedCategories.toTypedArray(), usedCategories.toTypedArray())
    }

    @Test
    fun test_create_poi_and_get_poi_by_id_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoi(testCreationPoi)
        val poi = SUT.getPoi(id.toString())

        assertNotNull(poi)
        assertEquals(id.toInt(), poi.id)

        assertEquals(testCreationPoi.title, poi.title)
        assertEquals(testCreationPoi.body, poi.body)
        assertEquals(testCreationPoi.contentLink, poi.contentLink)
        assertEquals(testCreationPoi.imageUrl, poi.imageUrl)
        assertEquals(testCreationPoi.creationDate.toEpochMilliseconds(), poi.creationDate.toEpochMilliseconds())
        assertEquals(testCreationPoi.commentsCount, poi.commentsCount)
        assertEquals(testCreationPoi.severity, poi.severity)
        Assert.assertArrayEquals(
            testCreationPoi.categories.sortedBy { it.id }.toTypedArray(),
            poi.categories.sortedBy { it.id }.toTypedArray()
        )
    }

    @Test
    fun test_create_poi_and_delete_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoi(testCreationPoi)
        val poi = SUT.getPoiList(OrderByColumns.DATE).first().first()

        assertNotNull(poi)

        SUT.deletePoi(id.toString())
        val poiList = SUT.getPoiList(OrderByColumns.DATE).first()
        assertTrue(poiList.isEmpty())
    }

    @Test
    fun test_create_poi_and_get_poi_list_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val expectedId = SUT.insertPoi(testCreationPoi)
        val poiList = SUT.getPoiList(OrderByColumns.DATE).first()
        assertTrue(poiList.find { it.id == expectedId.toInt() } != null)
    }

    @Test
    fun test_get_poi_list_ordered_by_date_DESC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi ->
            SUT.insertPoi(poi)
        }
        val poiList = SUT.getPoiList(OrderByColumns.DATE).first()

        Assert.assertArrayEquals(
            testCreationPoiList.sortedByDescending { it.creationDate }.map { it.id }.toTypedArray(),
            poiList.map { it.id }.toTypedArray()
        )
    }

    @Test
    fun test_get_poi_list_ordered_by_title_ASC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi ->
            SUT.insertPoi(poi)
        }
        val poiList = SUT.getPoiList(OrderByColumns.TITLE).first()

        Assert.assertArrayEquals(
            testCreationPoiList.sortedBy { it.title }.map { it.id }.toTypedArray(),
            poiList.map { it.id }.toTypedArray()
        )
    }

    @Test
    fun test_get_poi_list_ordered_by_severity_ASC_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi ->
            SUT.insertPoi(poi)
        }
        val poiList = SUT.getPoiList(OrderByColumns.SEVERITY).first()

        Assert.assertArrayEquals(
            testCreationPoiList.sortedBy { it.severity }.map { it.id }.toTypedArray(),
            poiList.map { it.id }.toTypedArray()
        )
    }

    @Test
    fun test_search_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi -> SUT.insertPoi(poi) }

        val query1 = "B T"
        val searchResult1 = SUT.searchPoi(query1)
        assertTrue(searchResult1.size == 1)
        assertEquals(searchResult1.first().id, 2)

        val query2 = "B Title"
        val searchResult2 = SUT.searchPoi(query2)
        assertTrue(searchResult2.size == 1)
        assertEquals(searchResult2.first().id, 2)

        val query3 = "C"
        val searchResult3 = SUT.searchPoi(query3)
        assertTrue(searchResult3.size == 3)

        val query4 = "www.google.com"
        val searchResult4 = SUT.searchPoi(query4)
        assertTrue(searchResult4.size == 3)
    }

    @Test
    fun test_add_comment_to_poi_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoi(testCreationPoi)

        SUT.addComment(
            PoiCommentDataModel(
                parentId = id.toInt(),
                id = UNSPECIFIED_ID,
                body = "Message",
                creationDataTime = Clock.System.now()
            )
        )

        val comment = SUT.getComments(id.toString()).first()

        assertTrue(comment.size == 1)
        assertEquals("Message", comment.first().body)
    }

    @Test
    fun test_delete_poi_also_deletes_comments() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val id = SUT.insertPoi(testCreationPoi)

        SUT.addComment(
            PoiCommentDataModel(
                parentId = id.toInt(),
                id = UNSPECIFIED_ID,
                body = "Message",
                creationDataTime = Clock.System.now()
            )
        )
        SUT.addComment(
            PoiCommentDataModel(
                parentId = id.toInt(),
                id = UNSPECIFIED_ID,
                body = "Message 2",
                creationDataTime = Clock.System.now()
            )
        )

        val commentsList = SUT.getComments(id.toString()).first()

        assertTrue(commentsList.size == 2)

        SUT.deletePoi(id.toString())

        val commentsListAfterDeleting = SUT.getComments(id.toString()).first()
        assertTrue(commentsListAfterDeleting.isEmpty())
    }

    @Test
    fun test_deletePoiOlderThen_deletes_poi__comments_and_used_categories_for_pois_older_then_5_days_success() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi -> SUT.insertPoi(poi) }
        SUT.addComment(
            PoiCommentDataModel(
                parentId = 1,
                id = UNSPECIFIED_ID,
                body = "Message 2",
                creationDataTime = Clock.System.now()
            )
        )
        SUT.addComment(
            PoiCommentDataModel(
                parentId = 3,
                id = UNSPECIFIED_ID,
                body = "Message 3",
                creationDataTime = Clock.System.now()
            )
        )

        val usedCategoriesCount = SUT.getUsedCategoriesIds().first().size
        assertEquals(testCreationPoiList.flatMap { it.categories.map { it.id } }.toSet().size, usedCategoriesCount)

        val deletedPoi = SUT.deletePoiOlderThen(5)
        assertEquals(2, deletedPoi.size)
        deletedPoi.forEach {
            val comments = SUT.getComments(it.id.toString()).first()
            assertTrue(comments.isEmpty())
        }

        val poiList = SUT.getPoiList(OrderByColumns.DATE).first()
        assertEquals(1, poiList.size)
        val comments = SUT.getComments(poiList.first().id.toString()).first()
        assertTrue(comments.isEmpty().not())
        val usedCategoriesCount2 = SUT.getUsedCategoriesIds().first().size
        assertEquals(poiList.first().categories.map { it.id }.size, usedCategoriesCount2)
    }

    @Test
    fun test_delete_poi_older_then_90_days_nothing_to_delete() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi -> SUT.insertPoi(poi) }
        val deletedPoi = SUT.deletePoiOlderThen(90)
        assertTrue(deletedPoi.isEmpty())
    }

    @Test
    fun test_delete_poi_also_deletes_used_categories() = runTest {
        categoriesDataSource.addCategories(testCategories)
        val poiId = SUT.insertPoi(testCreationPoi)
        val useCategoriesCount = SUT.getUsedCategoriesIds().first().size
        assertEquals(testCreationPoi.categories.size, useCategoriesCount)

        SUT.deletePoi(poiId.toString())
        val useCategoriesCount2 = SUT.getUsedCategoriesIds().first().size
        assertEquals(0, useCategoriesCount2)
    }

    @Test
    fun test_get_statistics_returns_statistics_values_based_on_test_input() = runTest {
        categoriesDataSource.addCategories(testCategories)
        testCreationPoiList.forEach { poi -> SUT.insertPoi(poi) }
        SUT.insertPoi(testCreationPoi)
        SUT.insertPoi(testCreationPoi)

        SUT.getPoi("1")
        SUT.getPoi("2")

        val statistics = SUT.getStatistics()

        assertEquals(2, statistics.viewedCount)
        assertEquals(3, statistics.unViewedCount)

        assertEquals(4, statistics.categoriesUsage.size)
        assertEquals(2, statistics.categoriesUsage["2"])
        assertEquals(2, statistics.categoriesUsage["6"])
        assertEquals(3, statistics.categoriesUsage["1"])
        assertEquals(3, statistics.categoriesUsage["5"])

        assertEquals(4, statistics.history.size)
    }

    private val testCreationPoi by lazy {
        PoiDataModel(
            id = UNSPECIFIED_ID,
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 0,
            severity = 0,
            categories = listOf(
                CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
                CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true)
            )
        )
    }

    private val testCreationPoiList by lazy {
        arrayListOf(
            PoiDataModel(
                id = 1,
                title = "A Title",
                body = "A Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 2.days,
                commentsCount = 0,
                severity = 0,
                categories = listOf(
                    CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            ),
            PoiDataModel(
                id = 2,
                title = "B Title",
                body = "B Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 7.days,
                commentsCount = 0,
                severity = 1,
                categories = listOf(
                    CategoryDataModel(2, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(6, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            ),
            PoiDataModel(
                id = 3,
                title = "C Title",
                body = "C Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 10.days,
                commentsCount = 0,
                severity = 1,
                categories = listOf(
                    CategoryDataModel(2, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(6, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            )
        )
    }

    private val testCategories by lazy {
        arrayListOf(
            CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(2, "Medium", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(3, "Normal", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(4, "Low", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true),
            CategoryDataModel(6, "Name 2", Color.WHITE, type = "PERSONAL", true),
            CategoryDataModel(7, "Name 3", Color.WHITE, type = "PERSONAL", true)
        )
    }
}