package com.grishko188.data.features.poi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.grishko188.data.features.categories.model.CategoryEntity

/**
 * Cross reference for many to many relationship between [PoiEntity] and [CategoryEntity]
 */
@Entity(
    tableName = "table_poi_to_category",
    primaryKeys = ["poi_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = PoiEntity::class,
            parentColumns = ["id"],
            childColumns = ["poi_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["poi_id"]),
        Index(value = ["category_id"]),
    ],
)
data class PoiWithCategoriesCrossRef(
    @ColumnInfo(name = "poi_id")
    val poiEntityId: Int,
    @ColumnInfo(name = "category_id")
    val categoryEntityId: Int,
)