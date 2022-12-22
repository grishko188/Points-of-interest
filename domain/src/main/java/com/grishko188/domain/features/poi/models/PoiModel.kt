package com.grishko188.domain.features.poi.models

import com.grishko188.domain.features.categories.models.Category
import kotlinx.datetime.Instant

data class PoiModel(
    val id: String,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val creationDate: Instant,
    val source: String?,
    val contentLink: String?,
    val commentsCount: Int,
    val categories: List<Category>
)