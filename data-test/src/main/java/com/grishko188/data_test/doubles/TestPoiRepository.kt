package com.grishko188.data_test.doubles

import android.graphics.Color
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.poi.models.*
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.Clock
import javax.inject.Inject

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
        categories = arrayListOf(Category("1", "Name", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
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
        categories = arrayListOf(Category("2", "Name 2", Color.GRAY, categoryType = CategoryType.PERSONAL, isMutable = true))
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
        categories = arrayListOf(Category("3", "Name 3", Color.GREEN, categoryType = CategoryType.PERSONAL, isMutable = true))
    )
)

class TestPoiRepository @Inject constructor() : PoiRepository {

    private val poiState = MutableSharedFlow<List<PoiModel>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        poiState.tryEmit(testPoiModels)
    }

    override fun getPoiList(sortOption: PoiSortOption?): Flow<List<PoiModel>> = poiState

    override fun getUsedCategories(): Flow<List<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchPoi(query: String): List<PoiModel> {
        return if (query == "Test query") poiState.replayCache.firstOrNull() ?: emptyList()
        else emptyList()
    }

    override suspend fun getDetailedPoi(id: String): PoiModel =
        poiState.replayCache.firstOrNull()?.find { it.id == id }!!

    override suspend fun createPoi(payload: PoiCreationPayload) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePoi(model: PoiModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGarbage(): Int {
        return 0
    }

    override suspend fun addComment(targetId: String, payload: PoiCommentPayload) {
        TODO("Not yet implemented")
    }

    override fun getComments(targetId: String): Flow<List<PoiComment>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion {
        TODO("Not yet implemented")
    }

    override suspend fun getStatistics(): PoiStatisticsSnapshot =
        PoiStatisticsSnapshot(emptyMap(), 0, 0, emptyMap())
}