package com.grishko188.data.features.poi.datasource

interface ImageDataSource {

    suspend fun copyLocalImage(uri: String): String

    suspend fun deleteImage(uri: String)
}