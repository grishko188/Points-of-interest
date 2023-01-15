package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetDetailedPoiUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<GetDetailedPoiUseCase.Params, PoiModel>(dispatcher) {

    override suspend fun operation(params: Params): PoiModel {
        return repository.getDetailedPoi(params.id)
    }

    data class Params(val id: String)
}