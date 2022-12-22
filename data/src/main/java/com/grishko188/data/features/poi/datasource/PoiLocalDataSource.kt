package com.grishko188.data.features.poi.datasource

import com.grishko188.data.features.poi.db.PoiDao
import com.grishko188.data.features.poi.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PoiLocalDataSource @Inject constructor(
    private val poiDao: PoiDao
) : PoiDataSource {

    override fun getPoiList(orderByOption: OrderByColumns): Flow<List<PoiDataModel>> =
        poiDao.getPoiList(orderByOption.columnName).map { list -> list.map { it.toDataModel() } }

    override fun getUsedCategoriesIds(): Flow<List<Int>> =
        poiDao.getUsedCategoriesIds()

    override suspend fun getPoi(id: String): PoiDataModel {
        return poiDao.getPoi(id.toInt()).toDataModel()
    }

    override suspend fun insertPoi(dataModel: PoiDataModel) {
        val entity = dataModel.toEntity()
        val categories = dataModel.categories.map { it.id }
        poiDao.insertPoiTransaction(entity, categories)
    }

    override suspend fun deletePoi(id: String) {
        poiDao.deletePoi(id.toInt())
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