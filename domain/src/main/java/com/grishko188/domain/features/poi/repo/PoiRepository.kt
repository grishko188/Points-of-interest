package com.grishko188.domain.features.poi.repo

import com.grishko188.domain.features.poi.models.*
import kotlinx.coroutines.flow.Flow

interface PoiRepository {

    fun getPoiList(): Flow<List<PoiSnapshotModel>>

    suspend fun getDetailedPoi(id: String): PoiDetailedModel

    suspend fun createPoi(payload: PoiCreationPayload): String

    suspend fun deletePoi(id: String)

    suspend fun addComment(targetId: String, payload: PoiCommentPayload)

    suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion
}