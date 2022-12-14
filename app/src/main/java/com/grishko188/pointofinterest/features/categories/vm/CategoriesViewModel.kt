package com.grishko188.pointofinterest.features.categories.vm

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.categories.interactor.*
import com.grishko188.pointofinterest.features.categories.ui.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {

    val detailedCategoriesUiState = MutableStateFlow<DetailedCategoriesUiState>(DetailedCategoriesUiState.Loading)

    val categoriesState = getCategoriesUseCase(Unit).map { list ->
        list.map { it.toUiModel() }.groupBy { it.categoryType.toTitle() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

    val itemsToDelete = MutableStateFlow<List<String>>(emptyList())

    fun onFetchDetailedState(categoryId: String?) {
        viewModelScope.launch {
            detailedCategoriesUiState.value = DetailedCategoriesUiState.Loading
            if (categoryId.isNullOrEmpty()) {
                detailedCategoriesUiState.value = DetailedCategoriesUiState.Success(null)
                return@launch
            }
            val selectedCategory = getCategoryUseCase(GetCategoryUseCase.Params(categoryId))
            detailedCategoriesUiState.value = DetailedCategoriesUiState.Success(selectedCategory.toUiModel())
        }
    }

    fun onUpdateItem(categoryUiModel: CategoryUiModel, name: String, color: Color) {
        viewModelScope.launch {
            val updated = categoryUiModel.copy(title = name, color = color).toDomainModel()
            updateCategoryUseCase(UpdateCategoryUseCase.Params(updated))
        }
    }

    fun onCreateItem(name: String, color: Color) {
        viewModelScope.launch {
            addCategoryUseCase(AddCategoryUseCase.Params(name, color.toArgb()))
        }
    }

    fun onDeleteItem(id: String) {
        val updatedList = itemsToDelete.value.toMutableList()
        updatedList.add(id)
        itemsToDelete.value = updatedList
    }

    fun onUndoDeleteItem(id: String) {
        val updatedList = itemsToDelete.value.toMutableList()
        updatedList.remove(id)
        itemsToDelete.value = updatedList
    }

    fun onCommitDeleteItem(id: String) {
        viewModelScope.launch {
            deleteCategoryUseCase(DeleteCategoryUseCase.Params(id))
        }
    }
}