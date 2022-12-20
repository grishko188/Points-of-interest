package com.grishko188.domain.features.poi.models

import com.grishko188.domain.features.categories.models.Category
import java.util.*

data class PoiDetailedModel(
    val id: String,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val creationDate: Date,
    val source: String?,
    val contentLink: String?,
    val comments: List<PoiComment>,
    val categories: List<Category>
)