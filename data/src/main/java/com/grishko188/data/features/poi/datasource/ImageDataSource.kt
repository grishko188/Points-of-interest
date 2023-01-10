package com.grishko188.data.features.poi.datasource

import java.io.InputStream

interface ImageDataSource {

    suspend fun copyLocalImage(uri: String): String

    suspend fun deleteImage(uri: String)

    fun getImage(uri: String): InputStream?
}