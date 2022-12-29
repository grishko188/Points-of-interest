package com.grishko188.pointofinterest.core.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
fun <T> retryableFlow(retryTrigger: RetryTrigger, flowProvider: () -> Flow<T>) =
    retryTrigger.retryEvent.flatMapLatest { flowProvider() }

class RetryTrigger {
    internal val retryEvent = MutableStateFlow(true)
    fun retry() {
        retryEvent.value = retryEvent.value.not()
    }
}