package com.grishko188.data.features.poi.model

import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.domain.features.poi.models.PoiComment
import com.grishko188.domain.features.poi.models.PoiCommentPayload
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class PoiCommentDataModel(
    val parentId: Int,
    val id: Int,
    val body: String,
    val creationDataTime: Instant
)

fun PoiCommentPayload.creationDataModel(parentId: Int) = PoiCommentDataModel(
    parentId = parentId,
    id = UNSPECIFIED_ID,
    body = body,
    creationDataTime = Clock.System.now()
)

fun PoiCommentEntity.toDataModel() = PoiCommentDataModel(
    parentId = parentId,
    id = id,
    body = body,
    creationDataTime = creationDataTime
)

fun PoiCommentDataModel.toEntity() = PoiCommentEntity(
    parentId = parentId,
    body = body,
    creationDataTime = creationDataTime
).apply { id = this@toEntity.id }

fun PoiCommentDataModel.toDomain() = PoiComment(
    id = id.toString(),
    message = body,
    commentDate = creationDataTime
)