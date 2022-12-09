package com.grishko188.pointofinterest.features.categories.ui.models

sealed class DetailedCategoriesUiState {
    object Loading : DetailedCategoriesUiState()
    data class Success(val categoryUiModel: CategoryUiModel?) : DetailedCategoriesUiState()
}

sealed class CategoriesListUiState {
    object Loading : CategoriesListUiState()
    data class Success(val categories: Map<String, List<CategoryUiModel>>)
}