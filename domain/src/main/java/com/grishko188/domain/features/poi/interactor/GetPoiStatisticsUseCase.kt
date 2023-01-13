package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import com.grishko188.domain.features.poi.models.*
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class GetPoiStatisticsUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val repository: PoiRepository
) : UseCase<Unit, PoiStatistics>() {

    override suspend fun operation(params: Unit): PoiStatistics {
        val snapshot = repository.getStatistics()
        val usedCategories = categoriesRepository.getCategories(snapshot.categoriesUsageCount.keys.map { it.toInt() }).single()
        return snapshot toStatisticsWith usedCategories
    }

    private infix fun PoiStatisticsSnapshot.toStatisticsWith(usedCategories: List<Category>): PoiStatistics {
        val categoriesUsage = hashMapOf<Category, Int>()
        categoriesUsageCount.forEach { (key, value) -> categoriesUsage[usedCategories.find { it.id == key }!!] = value }
        return PoiStatistics(
            viewsStatistics = PoiViewsStatistics(viewedPoiCount, unViewedPoiCount),
            categoriesUsageStatistics = PoiCategoriesUsageStatistics(categoriesUsage),
            creationStatistics = PoiCreationStatistics(poiAdditionsHistory)
        )
    }
}