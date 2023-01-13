package com.grishko188.data.poi.model

import com.grishko188.data.features.poi.model.PoiStatisticsDataModel
import com.grishko188.data.features.poi.model.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PoiStatisticsDataModelTest {

    @Test
    fun `test PoiStatisticsDataModelTest_toDomain() function returns PoiStatisticsSnapshot model with correct fields`() {
        val statsDataModel = PoiStatisticsDataModel(
            categoriesUsage = mapOf("1" to 10, "2" to 30),
            viewedCount = 10,
            unViewedCount = 30,
            history = mapOf(Clock.System.now() to 10, Clock.System.now() - 10.days to 30)
        )

        val domainModel = statsDataModel.toDomain()

        assertEquals(statsDataModel.viewedCount, domainModel.viewedPoiCount)
        assertEquals(statsDataModel.unViewedCount, domainModel.unViewedPoiCount)
        assert(domainModel.categoriesUsageCount.all { entry -> statsDataModel.categoriesUsage[entry.key] == entry.value })
        assert(domainModel.poiAdditionsHistory.all { entry -> statsDataModel.history[entry.key] == entry.value })
    }
}