package com.grishko188.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grishko188.data.features.categories.dao.CategoriesDao
import com.grishko188.data.features.categories.model.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PoiDatabase : RoomDatabase(){
    abstract fun categoriesDao(): CategoriesDao
}