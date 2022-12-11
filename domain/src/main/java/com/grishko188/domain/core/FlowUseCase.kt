package com.grishko188.domain.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<PARAMS, RESULT>(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    abstract suspend fun operation(params: PARAMS): Flow<RESULT>

    suspend operator fun invoke(params: PARAMS): Flow<RESULT> = operation(params).flowOn(dispatcher)
}