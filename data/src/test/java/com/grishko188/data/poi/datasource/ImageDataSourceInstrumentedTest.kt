@file:OptIn(ExperimentalCoroutinesApi::class)

package com.grishko188.data.poi.datasource

import com.grishko188.data.core.Local
import com.grishko188.data.features.poi.datasource.ImageDataSource
import com.grishko188.data_test.doubles.TestLocalImageDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class ImageDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var SUT: ImageDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_data_source_is_test_double() {
        assert(SUT is TestLocalImageDataSource)
    }

    @Test
    fun test_add_image_creates_new_image_file_in_cache_dir() = runTest {
        val mockUri = "content:///data/image/some.png"
        val cachedImagePath = SUT.copyLocalImage(mockUri)
        assertNotNull(cachedImagePath)
        assertTrue((SUT as TestLocalImageDataSource).assertImageExist(cachedImagePath))
    }

    @Test
    fun test_delete_image_deletes_file_from_cache_dir() = runTest {
        val mockUri = "content:///data/image/some.png"
        val cachedImagePath = SUT.copyLocalImage(mockUri)
        assertNotNull(cachedImagePath)
        SUT.deleteImage(cachedImagePath)
        assertFalse((SUT as TestLocalImageDataSource).assertImageExist(cachedImagePath))
    }
}