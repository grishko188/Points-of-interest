package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val poiRepository: PoiRepository
) : UseCase<DeleteCommentUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        poiRepository.deleteComment(params.id)
    }

    data class Params(val id: String)
}