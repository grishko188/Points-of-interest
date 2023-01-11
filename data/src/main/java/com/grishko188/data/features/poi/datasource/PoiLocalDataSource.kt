package com.grishko188.data.features.poi.datasource

import com.grishko188.data.features.poi.db.PoiDao
import com.grishko188.data.features.poi.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class PoiLocalDataSource @Inject constructor(
    private val poiDao: PoiDao
) : PoiDataSource {

    override fun getPoiList(orderByOption: OrderByColumns): Flow<List<PoiDataModel>> =
        poiDao.getPoiList(orderByOption.columnName).map { list -> list.map { it.toDataModel() } }

    override fun getUsedCategoriesIds(): Flow<List<Int>> =
        poiDao.getUsedCategoriesIds()

    override suspend fun searchPoi(query: String): List<PoiDataModel> =
        poiDao.searchPoi("*$query*").map { it.toDataModel() }

    override suspend fun getPoi(id: String): PoiDataModel {
        val fullEntity = poiDao.getPoi(id.toInt())
        if (fullEntity.entity.viewed.not()) poiDao.updatePoiViewed(id.toInt(), true)
        return fullEntity.toDataModel()
    }

    override suspend fun insertPoi(dataModel: PoiDataModel): Long {
        val entity = dataModel.toEntity()
        val categories = dataModel.categories.map { it.id }
        return poiDao.insertPoiTransaction(entity, categories)
    }

    override suspend fun deletePoi(id: String) {
        poiDao.deletePoi(id.toInt())
        poiDao.deleteUsedCategories(listOf(id.toInt()))
        poiDao.deleteCommentsForParent(id.toInt())
    }

    override suspend fun deletePoiOlderThen(daysCount: Int): List<PoiDataModel> {
        val dateThreshold = Clock.System.now() - daysCount.days
        val deletedItems = poiDao.deletePoiOlderThen(dateThreshold.toEpochMilliseconds())
        if (deletedItems.isNotEmpty()) {
            poiDao.deleteCommentsForParents(deletedItems.map { it.id })
        }
        return deletedItems.map { it.toDataModel() }
    }

    override fun getComments(parentId: String): Flow<List<PoiCommentDataModel>> =
        poiDao.getComments(parentId.toInt()).map { list -> list.map { it.toDataModel() } }

    override suspend fun addComment(comment: PoiCommentDataModel) {
        poiDao.insertCommentTransaction(comment.toEntity())
    }

    override suspend fun deleteComment(id: String) {
        poiDao.deleteComment(id.toInt())
    }
}