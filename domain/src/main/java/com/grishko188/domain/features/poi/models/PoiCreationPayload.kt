package com.grishko188.domain.features.poi.models

import com.grishko188.domain.features.categories.models.Category

data class PoiCreationPayload(
    val contentLink: String?,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val categories: List<Category>
)