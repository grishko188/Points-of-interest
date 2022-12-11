package com.grishko188.pointofinterest.features.categories.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel

fun Category.toUiModel() = CategoryUiModel(
    id = id.toString(),
    title = title,
    color = Color(color),
    isMutableCategory = isMutable,
    categoryType = categoryType
)

fun CategoryUiModel.toDomainModel() = Category(
    id = id.toInt(),
    title = title,
    color = color.toArgb(),
    isMutable = isMutableCategory,
    categoryType = categoryType
)