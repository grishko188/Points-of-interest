package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<DeleteCategoryUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.deleteCategory(params.categoryId)
    }

    data class Params(val categoryId: String)
}