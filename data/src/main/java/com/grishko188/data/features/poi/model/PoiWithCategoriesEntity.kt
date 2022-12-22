package com.grishko188.data.features.poi.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.grishko188.data.features.categories.model.CategoryEntity

data class PoiWithCategoriesEntity(
    @Embedded
    val entity: PoiEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PoiWithCategoriesCrossRef::class,
            parentColumn = "poi_id",
            entityColumn = "category_id",
        )
    )
    val categories: List<CategoryEntity>
)