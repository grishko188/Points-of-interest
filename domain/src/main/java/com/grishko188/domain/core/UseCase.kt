package com.grishko188.domain.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<PARAMS, RESULT>(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    abstract suspend fun operation(params: PARAMS): RESULT

    suspend operator fun invoke(params: PARAMS): RESULT = withContext(dispatcher) {
        operation(params)
    }
}