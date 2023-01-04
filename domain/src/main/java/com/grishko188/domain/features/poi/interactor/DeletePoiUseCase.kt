package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeletePoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<DeletePoiUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        repository.deletePoi(params.model)
    }

    data class Params(val model: PoiModel)
}