package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.poi.models.PoiCommentPayload
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<AddCommentUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.addComment(params.targetId, params.payload)
    }

    data class Params(val targetId: String, val payload: PoiCommentPayload)
}