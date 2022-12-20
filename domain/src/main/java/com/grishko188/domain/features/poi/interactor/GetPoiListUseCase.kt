package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.poi.models.PoiSnapshotModel
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoiListUseCase @Inject constructor(
    private val repository: PoiRepository
) : FlowUseCase<Unit, List<PoiSnapshotModel>>() {
    override fun operation(params: Unit): Flow<List<PoiSnapshotModel>> {
        return repository.getPoiList()
    }
}