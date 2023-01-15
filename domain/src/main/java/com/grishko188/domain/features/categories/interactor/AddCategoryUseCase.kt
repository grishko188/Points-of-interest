package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.categories.models.CreateCategoryPayload
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<AddCategoryUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.addCategory(params.toPayload())
    }

    private fun Params.toPayload() = CreateCategoryPayload(title, color)

    data class Params(
        val title: String,
        val color: Int
    )
}