package com.grishko188.domain.features.categories.models

data class Category(
    val id: String,
    val title: String,
    val color: Int,
    val isMutable: Boolean,
    val categoryType: CategoryType
)

enum class CategoryType {
    SEVERITY, GLOBAL, PERSONAL
}