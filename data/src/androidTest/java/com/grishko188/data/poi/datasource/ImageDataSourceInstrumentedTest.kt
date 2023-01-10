@file:OptIn(ExperimentalCoroutinesApi::class)

package com.grishko188.data.poi.datasource

import com.grishko188.data.core.Local
import com.grishko188.data.features.poi.datasource.ImageDataSource
import com.grishko188.data_test.doubles.TestLocalImageDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@HiltAndroidTest
class ImageDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var imageDataSource: ImageDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_data_source_is_test_double() {
        assert(imageDataSource is TestLocalImageDataSource)
    }

    @Test
    fun test_add_image_creates_new_image_file_in_cache_dir() = runTest {
        val mockUri = "content:///data/image/some.png"
        val cachedImagePath = imageDataSource.copyLocalImage(mockUri)
        assertNotNull(cachedImagePath)
        assertTrue((imageDataSource as TestLocalImageDataSource).assertImageExist(cachedImagePath))
    }

    @Test
    fun test_delete_image_deletes_file_from_cache_dir() = runTest {
        val mockUri = "content:///data/image/some.png"
        val cachedImagePath = imageDataSource.copyLocalImage(mockUri)
        assertNotNull(cachedImagePath)
        imageDataSource.deleteImage(cachedImagePath)
        assertFalse((imageDataSource as TestLocalImageDataSource).assertImageExist(cachedImagePath))
    }
}