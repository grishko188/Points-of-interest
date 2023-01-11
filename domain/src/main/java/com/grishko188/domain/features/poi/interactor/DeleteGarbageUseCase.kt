package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteGarbageUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<Unit, Int>() {
    override suspend fun operation(params: Unit): Int {
        return repository.deleteGarbage()
    }
}