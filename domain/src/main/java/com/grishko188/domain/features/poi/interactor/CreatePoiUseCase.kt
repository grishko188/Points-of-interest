package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.models.PoiCreationPayload
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class CreatePoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<CreatePoiUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        repository.createPoi(params.payload)
    }

    data class Params(val payload: PoiCreationPayload)
}