package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.models.PoiSortOption
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoiListUseCase @Inject constructor(
    private val repository: PoiRepository
) : FlowUseCase<GetPoiListUseCase.Params?, List<PoiModel>>() {

    override fun operation(params: Params?): Flow<List<PoiModel>> {
        return repository.getPoiList(params?.sortOption)
    }

    data class Params(val sortOption: PoiSortOption)
}