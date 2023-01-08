package com.grishko188.data.poi.datasource

import com.grishko188.data.core.Local
import com.grishko188.data.features.poi.datasource.ImageDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

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

}