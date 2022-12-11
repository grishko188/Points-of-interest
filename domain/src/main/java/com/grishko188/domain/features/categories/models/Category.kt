package com.grishko188.domain.features.categories.models

data class Category(
    val id: Int,
    val title: String,
    val color: Int,
    val isMutable: Boolean,
    val categoryType: CategoryType
)

enum class CategoryType {
    SEVERITY, GLOBAL, PERSONAL
}