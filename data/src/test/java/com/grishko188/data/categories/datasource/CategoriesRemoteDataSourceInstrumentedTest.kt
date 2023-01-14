package com.grishko188.data.categories.datasource

import com.grishko188.data.core.Remote
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class CategoriesRemoteDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Remote
    lateinit var SUT: CategoriesDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_categories_remote_data_source_provides_default_categories() = runTest {
        val defaultCategories = SUT.getCategories().first()

        assert(defaultCategories.isEmpty().not())
        assertEquals(4, defaultCategories.filter { it.type == "SEVERITY" }.size)
        val expected = arrayOf("GLOBAL", "SEVERITY")
        expected.sort()
        val actual = defaultCategories.groupBy { it.type }.keys.toTypedArray()
        actual.sort()
        Assert.assertArrayEquals(expected, actual)
    }
}