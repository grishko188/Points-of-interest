package com.grishko188.data.features.categories.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.categories.model.CategoryRemote
import com.grishko188.data.features.categories.model.toDataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.lang.reflect.Type
import javax.inject.Inject


class CategoriesFakeRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : CategoriesDataSource {

    var listType: Type = object : TypeToken<List<CategoryRemote>>() {}.type

    override fun getCategories(): Flow<List<CategoryDataModel>> = flow {
        emit(loadInitialCategories().map { it.toDataModel() })
    }

    private suspend fun loadInitialCategories(): List<CategoryRemote> {
        val json = getJson(PATH)
        return gson.fromJson(json, listType)
    }

    private suspend fun getJson(path: String): String =
        withContext(Dispatchers.IO) {
            run {
                context.assets.open(path).use {
                    val reader = BufferedReader(it.reader())
                    val content = StringBuilder()
                    var line = reader.readLine()
                    while (line != null) {
                        content.append(line)
                        line = reader.readLine()
                    }
                    content.toString()
                }
            }
        }

    companion object {
        private const val PATH = "categories/poi_categories_default.json"
    }
}