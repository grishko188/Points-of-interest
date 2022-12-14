package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<Unit, Unit>() {

    override suspend fun operation(params: Unit) {
        repository.sync()
    }
}