package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.models.PoiDetailedModel
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class GetDetailedPoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<GetDetailedPoiUseCase.Params, PoiDetailedModel>() {

    override suspend fun operation(params: Params): PoiDetailedModel {
        return repository.getDetailedPoi(params.id)
    }

    data class Params(val id: String)
}