package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesByIdsUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<GetCategoriesByIdsUseCase.Params, List<Category>>(dispatcher) {

    override fun operation(params: Params): Flow<List<Category>> = repository.getCategories(params.ids)

    data class Params(val ids: List<Int>)
}