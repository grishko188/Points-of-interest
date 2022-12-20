package com.grishko188.domain.features.poi.models

import com.grishko188.domain.features.categories.models.Category
import java.util.Date

data class PoiSnapshotModel(
    val id: String,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val creationDate: Date,
    val source: String?,
    val commentsCount: Int,
    val categories: List<Category>
)