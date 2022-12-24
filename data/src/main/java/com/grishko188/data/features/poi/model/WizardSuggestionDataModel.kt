package com.grishko188.data.features.poi.model

import com.grishko188.domain.features.poi.models.WizardSuggestion

data class WizardSuggestionDataModel(
    val contentUrl: String,
    val title: String? = null,
    val body: String? = null,
    val imageUrl: String? = null,
    val tags: List<String>? = null
)

fun WizardSuggestionDataModel.toDomain() = WizardSuggestion(
    contentUrl = contentUrl,
    title = title,
    body = body,
    imageUrl = imageUrl,
    tags = tags
)