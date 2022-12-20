package com.grishko188.domain.features.poi.models

data class WizardSuggestion(
    val title: String,
    val body: String,
    val imageUrl: String,
    val tags: List<String>
)