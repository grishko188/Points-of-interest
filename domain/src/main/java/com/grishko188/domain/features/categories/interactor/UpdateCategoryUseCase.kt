package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<UpdateCategoryUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.updateCategory(params.category)
    }

    data class Params(val category: Category)
}