package com.grishko188.data.features.poi.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.grishko188.data.features.poi.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {

    @Transaction
    @Query(
        value = """
               SELECT * FROM table_poi 
               ORDER BY
               CASE WHEN :column = 'creation_date_time' THEN creation_date_time END DESC,
               CASE WHEN :column = 'severity' THEN severity END ASC,
               CASE WHEN :column = 'title' THEN title END ASC
    """
    )
    fun getPoiList(column: String): Flow<List<PoiWithCategoriesEntity>>

    @Query(value = "SELECT DISTINCT category_id FROM table_poi_to_category")
    fun getUsedCategoriesIds(): Flow<List<Int>>

    @Transaction
    suspend fun insertPoiTransaction(entity: PoiEntity, categories: List<Int>) {
        val poiId = insertPoi(entity)
        val crossRefs = categories.map { categoryId ->
            PoiWithCategoriesCrossRef(
                poiEntityId = poiId.toInt(),
                categoryEntityId = categoryId
            )
        }
        insertOrIgnoreCategoryCrossRefEntities(crossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoi(entity: PoiEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCategoryCrossRefEntities(entities: List<PoiWithCategoriesCrossRef>)

    @Transaction
    @Query(value = "SELECT * FROM table_poi WHERE id = :id")
    suspend fun getPoi(id: Int): PoiWithCategoriesEntity

    @Query(value = " DELETE FROM table_poi WHERE id = :id")
    suspend fun deletePoi(id: Int)

    @Transaction
    suspend fun insertCommentTransaction(entity: PoiCommentEntity) {
        insertComment(entity)
        val count = commentsCount(entity.parentId)
        updatePoiCommentsCount(entity.parentId, count)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(entity: PoiCommentEntity)

    @Query(value = "UPDATE table_poi SET comments_count = :count WHERE id = :id")
    suspend fun updatePoiCommentsCount(id: Int, count: Int)

    @Query("SELECT COUNT(*) FROM table_poi_comments WHERE parent_id = :parentId")
    suspend fun commentsCount(parentId: Int): Int

    @Query(value = "SELECT * FROM table_poi_comments WHERE parent_id = :parentId")
    fun getComments(parentId: Int): Flow<List<PoiCommentEntity>>

    @Query(value = " DELETE FROM table_poi_comments WHERE id = :id")
    suspend fun deleteComment(id: Int)
}