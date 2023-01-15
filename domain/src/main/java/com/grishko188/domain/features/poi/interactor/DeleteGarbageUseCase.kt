package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteGarbageUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Int>(dispatcher) {
    override suspend fun operation(params: Unit): Int {
        return repository.deleteGarbage()
    }
}