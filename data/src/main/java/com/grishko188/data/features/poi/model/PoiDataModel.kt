package com.grishko188.data.features.poi.model

import android.net.Uri
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.categories.model.CategoryEntity
import com.grishko188.data.features.categories.model.toDataModel
import com.grishko188.data.features.categories.model.toDomain
import com.grishko188.domain.features.categories.models.*
import com.grishko188.domain.features.poi.models.PoiCreationPayload
import com.grishko188.domain.features.poi.models.PoiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class PoiDataModel(
    val id: Int,
    val contentLink: String?,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val creationDate: Instant,
    val severity: Int,
    val commentsCount: Int,
    val categories: List<CategoryDataModel>,
)

fun PoiWithCategoriesEntity.toDataModel() = entity.toDataModel(categories)

fun PoiEntity.toDataModel(categories: List<CategoryEntity> = emptyList()) = PoiDataModel(
    id = id,
    contentLink = contentLink,
    title = title,
    body = body,
    imageUrl = imageUrl,
    creationDate = creationDateTime,
    severity = severity,
    commentsCount = commentsCount,
    categories = categories.map { it.toDataModel() }
)

fun PoiDataModel.toEntity() = PoiEntity(
    contentLink = contentLink,
    title = title,
    body = body,
    imageUrl = imageUrl,
    creationDateTime = creationDate,
    severity = severity,
    commentsCount = commentsCount
).apply { id = this@toEntity.id }

fun PoiCreationPayload.creationDataModel() = PoiDataModel(
    id = UNSPECIFIED_ID,
    contentLink = contentLink,
    title = title,
    body = body,
    imageUrl = imageUrl,
    creationDate = Clock.System.now(),
    severity = categories.find { it.categoryType == CategoryType.SEVERITY }.toSeverityInt(),
    commentsCount = 0,
    categories = categories.map { it.toDataModel() }
)

fun PoiDataModel.toDomain() = PoiModel(
    id = id.toString(),
    title = title,
    body = body,
    imageUrl = imageUrl,
    creationDate = creationDate,
    source = contentLink?.extractSourceUrl(),
    contentLink = contentLink,
    commentsCount = commentsCount,
    categories = categories.map { it.toDomain() }
)

private fun String.extractSourceUrl() = Uri.parse(this).takeIf { it.scheme?.contains("http") == true}?.host

private fun Category?.toSeverityInt() = when (this?.title) {
    SEVERITY_HIGH -> 0
    SEVERITY_MEDIUM -> 1
    SEVERITY_NORMAL -> 2
    SEVERITY_LOW -> 3
    else -> Int.MAX_VALUE
}
