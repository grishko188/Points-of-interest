package com.grishko188.data_test.doubles

import android.content.Context
import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.grishko188.data.core.CacheFolder
import com.grishko188.data.features.poi.datasource.LocalImageDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class TestLocalImageDataSource @Inject constructor(
    @ApplicationContext context: Context,
    @CacheFolder cacheDir: File
) : LocalImageDataSource(context, cacheDir) {

    override fun getImage(uri: String): InputStream {
        return ByteArrayInputStream(mockImageByteArray())
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun assertImageExist(uri: String): Boolean {
        val imageName = Uri.parse(uri).lastPathSegment
        val file = File(cacheDir, imageName ?: "")
        return file.exists()
    }

    private fun mockImageByteArray() = byteArrayOf(0)
}