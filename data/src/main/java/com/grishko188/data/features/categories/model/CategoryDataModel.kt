package com.grishko188.data.features.categories.model

import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.categories.models.CreateCategoryPayload

data class CategoryDataModel(
    val id: Int,
    val title: String,
    val color: Int,
    val type: String,
    val isMutable: Boolean
) {
    companion object {
        val EMPTY = CategoryDataModel(-1, "", 0, "", false)
    }
}

fun CategoryDataModel.toDomain() = Category(
    id = id.toString(),
    title = title,
    color = color,
    categoryType = type.toCategoryType(),
    isMutable = isMutable
)

fun Category.toDataModel() = CategoryDataModel(
    id = id.toInt(),
    title = title,
    color = color,
    type = categoryType.name,
    isMutable = isMutable
)

fun CreateCategoryPayload.toDataModel() = CategoryDataModel(
    id = UNSPECIFIED_ID,
    title = title,
    color = color,
    type = CategoryType.PERSONAL.name,
    isMutable = true
)

private fun String.toCategoryType() = CategoryType.valueOf(this)