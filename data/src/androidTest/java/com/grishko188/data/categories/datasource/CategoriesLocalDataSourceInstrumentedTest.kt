package com.grishko188.data.categories.datasource

import android.graphics.Color
import com.grishko188.data.core.Local
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class CategoriesLocalDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var SUT: CategoriesDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }


    private fun testDataModels() = arrayListOf(
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 1", color = Color.WHITE, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 2", color = Color.BLACK, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 3", color = Color.YELLOW, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Title 3", color = Color.GREEN, type = "PERSONAL", isMutable = true),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Global 1", color = Color.RED, type = "GLOBAL", isMutable = false),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Global 2", color = Color.BLUE, type = "GLOBAL", isMutable = false),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Global 3", color = Color.GREEN, type = "GLOBAL", isMutable = false),
        CategoryDataModel(id = UNSPECIFIED_ID, title = "Global 4", color = Color.MAGENTA, type = "GLOBAL", isMutable = false),
    )
}