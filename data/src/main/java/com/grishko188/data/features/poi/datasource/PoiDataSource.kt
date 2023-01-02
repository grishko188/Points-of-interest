package com.grishko188.data.features.poi.datasource

import com.grishko188.data.features.poi.model.PoiCommentDataModel
import com.grishko188.data.features.poi.model.PoiDataModel
import com.grishko188.data.features.poi.model.OrderByColumns
import kotlinx.coroutines.flow.Flow

interface PoiDataSource {

    fun getPoiList(orderByOption: OrderByColumns): Flow<List<PoiDataModel>>
    fun getUsedCategoriesIds(): Flow<List<Int>>
    suspend fun searchPoi(query: String): List<PoiDataModel>
    suspend fun getPoi(id: String): PoiDataModel
    suspend fun insertPoi(dataModel: PoiDataModel)
    suspend fun deletePoi(id: String)

    fun getComments(parentId: String): Flow<List<PoiCommentDataModel>>
    suspend fun addComment(comment: PoiCommentDataModel)
    suspend fun deleteComment(id: String)
}