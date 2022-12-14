package com.grishko188.data.features.categories.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grishko188.data.core.UNSPECIFIED_ID

@Entity(
    tableName = "table_categories",
)
data class CategoryEntity(
    val title: String,
    val color: Int,
    val type: String,
    val isMutable: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = UNSPECIFIED_ID
}

fun CategoryEntity.toDataModel() = CategoryDataModel(id, title, color, type, isMutable)

fun CategoryDataModel.toEntity(): CategoryEntity {
    val entity = CategoryEntity(title, color, type, isMutable)
    if (this.id != UNSPECIFIED_ID) {
        entity.id = id
    }
    return entity
}