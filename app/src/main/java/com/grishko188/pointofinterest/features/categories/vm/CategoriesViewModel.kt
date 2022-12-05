package com.grishko188.pointofinterest.features.categories.vm

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor() : ViewModel() {

    val categoriesState = MutableStateFlow<List<CategoryUiModel>>(emptyList())

    init {
        viewModelScope.launch {
            categoriesState.emitAll(collectCategories())
        }
    }

    fun onDeleteItem(id: String) {
        val updateList = categoriesState.value.toMutableList()
        updateList.removeAll { it.id == id }
        categoriesState.value = updateList
    }

    private fun collectCategories(): Flow<List<CategoryUiModel>> = flow {
        emit(mockCategories())
    }

    private fun mockCategories(): List<CategoryUiModel> = arrayListOf(
        CategoryUiModel(id = "_ID3", title = "High", color = Color(0xFFD50000)),
        CategoryUiModel(id = "_ID5", title = "Medium", color = Color(0xFFFF9800)),
        CategoryUiModel(id = "_ID6", title = "Low", color = Color(0xFF7CB342)),
        CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9)),
        CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF9C27B0)),
        CategoryUiModel(id = "_ID7", title = "TV Shows", color = Color(0xFFC6FF00)),
        CategoryUiModel(id = "_ID8", title = "Home decor", color = Color(0xFF00897B)),
        CategoryUiModel(id = "_ID9", title = "Sport", color = Color(0xFFFFEB3B)),
        CategoryUiModel(id = "_ID10", title = "Android development", color = Color(0xFF76FF03)),
    )

}