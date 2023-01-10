package com.grishko188.data.poi.repository

import android.graphics.Color
import android.net.Uri
import com.grishko188.data.MockitoHelper
import com.grishko188.data.MockitoHelper.anyNonNull
import com.grishko188.data.MockitoHelper.argumentCaptor
import com.grishko188.data.MockitoHelper.capture
import com.grishko188.data.MockitoHelper.whenever
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.categories.model.toDataModel
import com.grishko188.data.features.poi.datasource.*
import com.grishko188.data.features.poi.model.*
import com.grishko188.data.features.poi.repository.PoiRepositoryImpl
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.poi.models.PoiCommentPayload
import com.grishko188.domain.features.poi.models.PoiCreationPayload
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.models.PoiSortOption
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PoiRepositoryTest {

    @Mock
    private lateinit var localPoiDataSource: PoiDataSource

    @Mock
    private lateinit var wizardRemoteDataSource: WizardDataSource

    @Mock
    private lateinit var localImageDataSource: ImageDataSource

    private lateinit var uriMockStatic: MockedStatic<Uri>

    private lateinit var SUT: PoiRepository

    @Before
    fun setup() {
        SUT = PoiRepositoryImpl(localPoiDataSource, localImageDataSource, wizardRemoteDataSource)

        val mockUri = MockitoHelper.mock<Uri>()
        uriMockStatic = Mockito.mockStatic(Uri::class.java)
        uriMockStatic.`when`<Uri> { Uri.parse("https://www.google.com/somethingelse?query=1") }.thenReturn(mockUri)
        whenever(mockUri.scheme).thenReturn("http")
        whenever(mockUri.host).thenReturn("www.google.com")
    }

    @After
    fun dispose() {
        uriMockStatic.close()
    }

    @Test
    fun `test getPoiList invokes local poi data source getPoiList function with correct sort options`() = runTest {

        val testPoiList = testPoiDataModels()
        val sortOption = PoiSortOption.SEVERITY
        whenever(localPoiDataSource.getPoiList(anyNonNull())).thenReturn(flowOf(testPoiList))

        val result = SUT.getPoiList(sortOption).first()
        val captor = argumentCaptor<OrderByColumns>()

        verify(localPoiDataSource, times(1)).getPoiList(capture(captor))
        assertEquals(OrderByColumns.SEVERITY, captor.allValues[0])
        Assert.assertArrayEquals(testPoiList.map { it.toDomain() }.toTypedArray(), result.toTypedArray())

    }

    @Test
    fun `test getPoiList invokes local poi data source getPoiList function with default sort options when argument is null`() = runTest {

        val testPoiList = testPoiDataModels()
        whenever(localPoiDataSource.getPoiList(anyNonNull())).thenReturn(flowOf(testPoiList))

        SUT.getPoiList(null).first()
        val captor = argumentCaptor<OrderByColumns>()

        verify(localPoiDataSource, times(1)).getPoiList(capture(captor))
        assertEquals(OrderByColumns.DATE, captor.value)
    }

    @Test
    fun `test getUsedCategories invokes local data source getUsedCategories function`() = runTest {
        val categories = arrayListOf(1, 2, 3)
        whenever(localPoiDataSource.getUsedCategoriesIds()).thenReturn(flowOf(categories))

        val result = SUT.getUsedCategories().first()
        verify(localPoiDataSource, times(1)).getUsedCategoriesIds()

        Assert.assertArrayEquals(categories.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun `test searchPoi invokes local data source searchPoi function with same query`() = runTest {

        val query = "Title 1"
        val testPoiList = testPoiDataModels()
        whenever(localPoiDataSource.searchPoi(anyNonNull())).thenReturn(testPoiList)

        val result = SUT.searchPoi(query)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).searchPoi(capture(captor))

        assertEquals(query, captor.value)
        Assert.assertArrayEquals(testPoiList.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun `test getDetailedPoi invokes local data source getPoi function`() = runTest {

        val id = "1"
        val fakePoi = PoiDataModel(
            id = 1,
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = "PERSONAL", true))
        )

        whenever(localPoiDataSource.getPoi(anyNonNull())).thenReturn(fakePoi)

        val result = SUT.getDetailedPoi(id)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).getPoi(capture(captor))

        assertEquals(id, captor.value)
        assertEquals(fakePoi.toDomain(), result)
    }

    @Test
    fun `test createPoi invokes local data source insertPoi function and not invoke local image data source copyLocalImage with remote image`() =
        runTest {
            val payload = PoiCreationPayload(
                title = "Title",
                body = "Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                categories = arrayListOf(
                    Category(
                        id = "1",
                        title = "Name",
                        color = Color.WHITE,
                        categoryType = CategoryType.PERSONAL,
                        isMutable = true
                    )
                )
            )
            whenever(localPoiDataSource.insertPoi(anyNonNull())).thenReturn(1)

            SUT.createPoi(payload)
            val captor = argumentCaptor<PoiDataModel>()

            verify(localPoiDataSource, times(1)).insertPoi(capture(captor))
            verify(localImageDataSource, times(0)).copyLocalImage(anyNonNull())


            assertEquals(UNSPECIFIED_ID, captor.value.id)
            assertEquals(payload.title, captor.value.title)
            assertEquals(payload.body, captor.value.body)
            assertEquals(payload.imageUrl, captor.value.imageUrl)
            assertEquals(payload.contentLink, captor.value.contentLink)
            Assert.assertArrayEquals(payload.categories.map { it.toDataModel() }.toTypedArray(), captor.value.categories.toTypedArray())
        }

    @Test
    fun `test createPoi invokes local data source insertPoi function and local image data source copyLocalImage with local image uri`() =
        runTest {
            val payload = PoiCreationPayload(
                title = "Title",
                body = "Body",
                contentLink = null,
                imageUrl = "content:///file/something/image.png",
                categories = arrayListOf(
                    Category(
                        id = "1",
                        title = "Name",
                        color = Color.WHITE,
                        categoryType = CategoryType.PERSONAL,
                        isMutable = true
                    )
                )
            )

            val fileImagePath = "file:///storage/poi_1235.jpg"

            whenever(localPoiDataSource.insertPoi(anyNonNull())).thenReturn(1)
            whenever(localImageDataSource.copyLocalImage(anyNonNull())).thenReturn(fileImagePath)

            SUT.createPoi(payload)
            val captor = argumentCaptor<PoiDataModel>()
            val captorImageUri = argumentCaptor<String>()

            verify(localPoiDataSource, times(1)).insertPoi(capture(captor))
            verify(localImageDataSource, times(1)).copyLocalImage(capture(captorImageUri))

            assertEquals(UNSPECIFIED_ID, captor.value.id)
            assertEquals(payload.title, captor.value.title)
            assertEquals(payload.body, captor.value.body)
            assertEquals(payload.imageUrl, captorImageUri.value)
            assertEquals(fileImagePath, captor.value.imageUrl)
            assertEquals(payload.contentLink, captor.value.contentLink)
            Assert.assertArrayEquals(payload.categories.map { it.toDataModel() }.toTypedArray(), captor.value.categories.toTypedArray())
        }

    @Test
    fun `test deletePoi invokes local data source deletePoi function and local image data source deleteImage with local image uri`() =
        runTest {
            val poiModel = PoiModel(
                id = "1",
                title = "Title",
                body = "Body",
                imageUrl = "https://www.google.com/image",
                source = "www.google.com",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now(),
                commentsCount = 2,
                categories = arrayListOf(Category("1", "Name", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
            )

            whenever(localPoiDataSource.deletePoi(anyNonNull())).thenReturn(Unit)

            SUT.deletePoi(poiModel)
            val captor = argumentCaptor<String>()

            verify(localPoiDataSource, times(1)).deletePoi(capture(captor))
            verify(localImageDataSource, times(0)).deleteImage(anyNonNull())
            assertEquals(poiModel.id, captor.value)
        }

    @Test
    fun `test deletePoi invokes local data source deletePoi function and not invoke local image data source deleteImage with remote image uri`() =
        runTest {
            val fileImagePath = "file:///storage/poi_1235.jpg"

            val poiModel = PoiModel(
                id = "1",
                title = "Title",
                body = "Body",
                imageUrl = fileImagePath,
                source = "www.google.com",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now(),
                commentsCount = 2,
                categories = arrayListOf(Category("1", "Name", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
            )

            whenever(localPoiDataSource.deletePoi(anyNonNull())).thenReturn(Unit)
            whenever(localImageDataSource.deleteImage(anyNonNull())).thenReturn(Unit)

            SUT.deletePoi(poiModel)
            val captor = argumentCaptor<String>()
            val captorImageUri = argumentCaptor<String>()

            verify(localPoiDataSource, times(1)).deletePoi(capture(captor))
            verify(localImageDataSource, times(1)).deleteImage(capture(captorImageUri))

            assertEquals(poiModel.id, captor.value)
            assertEquals(poiModel.imageUrl, captorImageUri.value)
        }

    @Test
    fun `test deleteGarbage invokes local data source deletePoiOlderThen function and  invoke local image data source deleteImage for local uris`() =
        runTest {
            val fileImagePath = "file:///storage/poi_1235.jpg"
            val deletedPoiList = arrayListOf(
                PoiDataModel(
                    id = 1,
                    title = "Title",
                    body = "Body",
                    imageUrl = fileImagePath,
                    contentLink = "https://www.google.com/somethingelse?query=1",
                    creationDate = Clock.System.now(),
                    commentsCount = 2,
                    severity = 0,
                    categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true))
                ),
                PoiDataModel(
                    id = 2,
                    title = "Title 2",
                    body = "Body 3",
                    imageUrl = "https://www.google.com/somethingelse?query=1",
                    contentLink = "https://www.google.com/somethingelse?query=1",
                    creationDate = Clock.System.now(),
                    commentsCount = 2,
                    severity = 0,
                    categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true))
                )
            )

            whenever(localPoiDataSource.deletePoiOlderThen(90)).thenReturn(deletedPoiList)
            whenever(localImageDataSource.deleteImage(anyNonNull())).thenReturn(Unit)

            val captor = argumentCaptor<Int>()
            val captorImageUri = argumentCaptor<String>()

            SUT.deleteGarbage()

            verify(localPoiDataSource, times(1)).deletePoiOlderThen(capture(captor))
            verify(localImageDataSource, times(1)).deleteImage(capture(captorImageUri))

            assertEquals(fileImagePath, captorImageUri.value)
            assertEquals(90, captor.value)
        }

    @Test
    fun `test addComment invokes local data source addComment function`() = runTest {
        val payload = PoiCommentPayload(body = "Message")
        val parentId = "1"

        whenever(localPoiDataSource.addComment(anyNonNull())).thenReturn(Unit)

        SUT.addComment(parentId, payload)
        val commentCaptor = argumentCaptor<PoiCommentDataModel>()

        verify(localPoiDataSource, times(1)).addComment(capture(commentCaptor))

        assertEquals(UNSPECIFIED_ID, commentCaptor.value.id)
        assertEquals(parentId, commentCaptor.value.parentId.toString())
        assertEquals(payload.body, commentCaptor.value.body)
        assertNotNull(commentCaptor.value.creationDataTime)
    }

    @Test
    fun `test deleteComment invokes local data source deleteComment function`() = runTest {
        val commentId = "1"
        whenever(localPoiDataSource.deleteComment(anyNonNull())).thenReturn(Unit)
        SUT.deleteComment(commentId)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).deleteComment(capture(captor))
        assertEquals(commentId, captor.value)
    }

    @Test
    fun `test getComments invokes local data source getComments function`() = runTest {
        val parentId = "105"
        val fakeComments = arrayListOf(
            PoiCommentDataModel(
                parentId = 105,
                id = 1,
                body = "Comment body 1",
                creationDataTime = Clock.System.now()
            ),
            PoiCommentDataModel(
                parentId = 105,
                id = 2,
                body = "Comment body 2",
                creationDataTime = Clock.System.now()
            )
        )

        whenever(localPoiDataSource.getComments(anyNonNull())).thenReturn(flowOf(fakeComments))
        val result = SUT.getComments(parentId).first()
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).getComments(capture(captor))
        assertEquals(parentId, captor.value)
        Assert.assertArrayEquals(fakeComments.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun `test getWizardSuggestion invokes remote wizard data source getWizardSuggestion function`() = runTest {
        val contentUrl = "https://www.google.com"
        val fakeWizardSuggestion = WizardSuggestionDataModel(
            contentUrl = "https://www.google.com",
            title = "Title",
            body = "Suggestion body",
            imageUrl = "https://www.google.com/image"
        )
        whenever(wizardRemoteDataSource.getWizardSuggestion(anyNonNull())).thenReturn(fakeWizardSuggestion)
        val result = SUT.getWizardSuggestion(contentUrl)
        val captor = argumentCaptor<String>()
        verify(wizardRemoteDataSource, times(1)).getWizardSuggestion(capture(captor))
        assertEquals(contentUrl, captor.value)
        assertEquals(fakeWizardSuggestion.toDomain(), result)
    }

    private fun testPoiDataModels(): List<PoiDataModel> {
        val poiStub1 = PoiDataModel(
            id = 1,
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = "PERSONAL", true))
        )

        val poiStub2 = PoiDataModel(
            id = 2,
            title = "Title 2",
            body = "Body 2",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 0,
            severity = 1,
            categories = arrayListOf(CategoryDataModel(2, "Name 2", Color.GRAY, type = "PERSONAL", true))
        )

        val poiStub3 = PoiDataModel(
            id = 3,
            title = "Title 3",
            body = "Body 3",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 10,
            severity = 0,
            categories = arrayListOf(CategoryDataModel(3, "Name 3", Color.GREEN, type = "PERSONAL", true))
        )

        return arrayListOf(poiStub1, poiStub2, poiStub3)
    }
}