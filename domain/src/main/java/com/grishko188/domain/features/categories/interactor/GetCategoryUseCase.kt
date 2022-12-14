package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : FlowUseCase<GetCategoryUseCase.Params, Category>() {

    override suspend fun operation(params: Params): Flow<Category> = repository.getCategory(params.categoryId)

    data class Params(val categoryId: String)
}