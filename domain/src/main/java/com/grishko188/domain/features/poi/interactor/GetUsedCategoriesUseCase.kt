package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.FlowUseCase

import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsedCategoriesUseCase @Inject constructor(
    private val repository: PoiRepository
) : FlowUseCase<Unit, List<Int>>() {
    override fun operation(params: Unit): Flow<List<Int>> = repository.getUsedCategories()
}