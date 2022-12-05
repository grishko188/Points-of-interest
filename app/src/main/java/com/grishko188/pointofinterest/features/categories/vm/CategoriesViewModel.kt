package com.grishko188.pointofinterest.features.categories.vm

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.pointofinterest.core.utils.containsId
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor() : ViewModel() {

    val categoriesState = MutableStateFlow<Map<String, List<CategoryUiModel>>>(emptyMap())

    init {
        viewModelScope.launch {
            categoriesState.emitAll(collectCategories())
        }
    }

    fun onDeleteItem(id: String) {
        val updatedMap = categoriesState.value.toMutableMap()
        val entry = updatedMap.entries.find { it.value.containsId(id) }
        entry?.value?.let {
            val updatedList = entry.value.toMutableList()
            updatedList.removeAll { it.id == id }
            updatedMap[entry.key] = updatedList
            categoriesState.value = updatedMap
        }
    }

    private fun collectCategories(): Flow<Map<String, List<CategoryUiModel>>> = flow {
        emit(mockCategories())
    }

    private fun mockCategories(): Map<String, List<CategoryUiModel>> = hashMapOf<String, List<CategoryUiModel>>().apply {

        put(
            "Severity",
            arrayListOf(
                CategoryUiModel(id = "_ID3", title = "High", color = Color(0xFFD50000)),
                CategoryUiModel(id = "_ID5", title = "Medium", color = Color(0xFFFF9800)),
                CategoryUiModel(id = "_ID6", title = "Low", color = Color(0xFF7CB342))
            )
        )

        put(
            "Global",
            arrayListOf(
                CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9)),
                CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF9C27B0)),
                CategoryUiModel(id = "_ID7", title = "Video", color = Color(0xFFC6FF00)),
                CategoryUiModel(id = "_ID8", title = "Estate", color = Color(0xFF00897B)),
                CategoryUiModel(id = "_ID9", title = "Sport", color = Color(0xFFFFEB3B)),
                CategoryUiModel(id = "_ID10", title = "Work", color = Color(0xFF1E40AF))
            )
        )

        put(
            "Focused",
            arrayListOf(
                CategoryUiModel(id = "_ID11", title = "Android development", color = Color(0xFF76FF03)),
                CategoryUiModel(id = "_ID11", title = "Abelton", color = Color(0xFF93C5FD)),
                CategoryUiModel(id = "_ID11", title = "Football", color = Color(0xFFA8A29E)),
                CategoryUiModel(id = "_ID11", title = "Apartments", color = Color(0xFFFB923C)),
            )
        )
    }
}