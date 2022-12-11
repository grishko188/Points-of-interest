package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<UpdateCategoryUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        repository.updateCategory(params.category)
    }

    data class Params(val category: Category)
}