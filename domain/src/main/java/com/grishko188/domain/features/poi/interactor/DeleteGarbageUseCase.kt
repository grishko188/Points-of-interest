package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteGarbageUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<Unit, Unit>() {
    override suspend fun operation(params: Unit) {
        repository.deleteGarbage()
    }
}