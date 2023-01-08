package com.grishko188.data.poi.db

import com.grishko188.data.features.poi.db.PoiDao
import com.grishko188.data.features.poi.model.OrderByColumns
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class PoiDaoInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var SUT: PoiDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_poi_table_is_empty() = runTest {
        val count = SUT.getPoiList(OrderByColumns.DATE.columnName).first().size
        assertEquals(0, count)
    }
}