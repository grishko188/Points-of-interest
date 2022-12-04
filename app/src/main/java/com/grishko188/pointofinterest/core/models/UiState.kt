package com.grishko188.pointofinterest.core.models

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class Error(val message: String, val showRetryButton: Boolean = true) : UiState()
    data class Success<T>(val data: T) : UiState()
}