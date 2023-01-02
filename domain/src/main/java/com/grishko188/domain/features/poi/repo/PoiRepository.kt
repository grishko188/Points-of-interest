package com.grishko188.domain.features.poi.repo

import com.grishko188.domain.features.poi.models.*
import kotlinx.coroutines.flow.Flow

interface PoiRepository {

    fun getPoiList(sortOption: PoiSortOption?): Flow<List<PoiModel>>

    fun getUsedCategories(): Flow<List<Int>>

    suspend fun searchPoi(query: String): List<PoiModel>

    suspend fun getDetailedPoi(id: String): PoiModel

    suspend fun createPoi(payload: PoiCreationPayload)

    suspend fun deletePoi(id: String)

    suspend fun addComment(targetId: String, payload: PoiCommentPayload)

    fun getComments(targetId: String): Flow<List<PoiComment>>

    suspend fun deleteComment(id: String)

    suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion
}