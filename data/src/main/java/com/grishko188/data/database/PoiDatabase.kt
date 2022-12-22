package com.grishko188.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grishko188.data.features.categories.dao.CategoriesDao
import com.grishko188.data.features.categories.model.CategoryEntity
import com.grishko188.data.features.poi.db.PoiDao
import com.grishko188.data.features.poi.model.PoiCommentEntity
import com.grishko188.data.features.poi.model.PoiEntity
import com.grishko188.data.features.poi.model.PoiWithCategoriesCrossRef

@Database(
    entities = [
        CategoryEntity::class,
        PoiEntity::class,
        PoiWithCategoriesCrossRef::class,
        PoiCommentEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
abstract class PoiDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun poiDao(): PoiDao
}