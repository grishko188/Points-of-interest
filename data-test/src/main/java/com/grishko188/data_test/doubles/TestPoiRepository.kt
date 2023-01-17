package com.grishko188.data_test.doubles

import android.graphics.Color
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.poi.models.*
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.random.Random

val testPoiModels = arrayListOf(
    PoiModel(
        id = "1",
        title = "Title",
        body = "Body",
        imageUrl = "https://www.google.com/image",
        contentLink = "https://www.google.com/somethingelse?query=1",
        source = "test",
        creationDate = Clock.System.now(),
        commentsCount = 2,
        categories = arrayListOf(Category("1", "Title", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
    ),

    PoiModel(
        id = "2",
        title = "Title 2",
        body = "Body 2",
        imageUrl = "https://www.google.com/image",
        contentLink = "https://www.google.com/somethingelse?query=1",
        creationDate = Clock.System.now(),
        source = "test",
        commentsCount = 0,
        categories = arrayListOf(Category("2", "Title 2", Color.BLACK, categoryType = CategoryType.PERSONAL, isMutable = true))
    ),

    PoiModel(
        id = "3",
        title = "Title 3",
        body = "Body 3",
        imageUrl = "https://www.google.com/image",
        contentLink = "https://www.google.com/somethingelse?query=1",
        creationDate = Clock.System.now(),
        commentsCount = 10,
        source = "test",
        categories = arrayListOf(Category("3", "Title 3", Color.RED, categoryType = CategoryType.PERSONAL, isMutable = true))
    )
)

val testComments = mutableListOf(
    PoiComment("1", "Comment 1", Clock.System.now()),
    PoiComment("2", "Comment 2", Clock.System.now()),
    PoiComment("3", "Comment 3", Clock.System.now())
)

class TestPoiRepository @Inject constructor() : PoiRepository {

    private val poiState = MutableStateFlow<List<PoiModel>>(testPoiModels)
    private val commentsState = MutableStateFlow(testComments)

    override fun getPoiList(sortOption: PoiSortOption?): Flow<List<PoiModel>> = poiState

    override fun getUsedCategories(): Flow<List<Int>> =
        poiState.map { list -> list.map { it.categories }.flatten().map { it.id.toInt() } }

    override suspend fun searchPoi(query: String): List<PoiModel> {
        return if (query == "Test query") poiState.replayCache.firstOrNull() ?: emptyList()
        else emptyList()
    }

    override suspend fun getDetailedPoi(id: String): PoiModel =
        poiState.replayCache.firstOrNull()?.find { it.id == id }!!

    override suspend fun createPoi(payload: PoiCreationPayload) {
        throw IllegalStateException("Use mock for this function")
    }

    override suspend fun deletePoi(model: PoiModel) {
        val updatedPoiList = poiState.value.toMutableList()
        updatedPoiList.remove(model)
        poiState.tryEmit(updatedPoiList)
    }

    override suspend fun deleteGarbage(): Int {
        return 0
    }

    override suspend fun addComment(targetId: String, payload: PoiCommentPayload) {
        val commentToAdd = PoiComment(Random.nextInt().toString(), payload.body, Clock.System.now())
        val updatedList = commentsState.value.toMutableList()
        updatedList.add(commentToAdd)
        commentsState.tryEmit(updatedList)
    }

    override fun getComments(targetId: String): Flow<List<PoiComment>> = commentsState

    override suspend fun deleteComment(id: String) {
        val commentsToUpdate = commentsState.value.toMutableList()
        commentsToUpdate.removeIf { it.id == id }
        commentsState.tryEmit(commentsToUpdate)
    }

    override suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion {
        throw IllegalStateException("Use mock for this function")
    }

    override suspend fun getStatistics(): PoiStatisticsSnapshot =
        PoiStatisticsSnapshot(emptyMap(), 0, 0, emptyMap())
}