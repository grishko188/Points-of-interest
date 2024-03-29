package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SearchPoiUseCase @Inject constructor(
    private val poiRepository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<SearchPoiUseCase.Params, List<PoiModel>>(dispatcher) {

    override suspend fun operation(params: Params): List<PoiModel> =
        poiRepository.searchPoi(params.query)

    data class Params(val query: String)
}