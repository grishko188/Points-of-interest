package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : FlowUseCase<Unit, List<Category>>() {
    override fun operation(params: Unit): Flow<List<Category>> = repository.getCategories()
}