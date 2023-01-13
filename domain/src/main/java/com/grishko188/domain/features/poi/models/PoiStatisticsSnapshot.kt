package com.grishko188.domain.features.poi.models

import kotlinx.datetime.Instant

class PoiStatisticsSnapshot(
    val categoriesUsageCount: Map<String, Int>,
    val viewedPoiCount: Int,
    val unViewedPoiCount: Int,
    val poiAdditionsHistory: Map<Instant, Int>
)