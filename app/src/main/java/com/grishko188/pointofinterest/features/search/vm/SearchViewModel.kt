package com.grishko188.pointofinterest.features.search.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.poi.interactor.SearchPoiUseCase
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.features.home.ui.models.toListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchVm @Inject constructor(
    private val searchPoiUseCase: SearchPoiUseCase
) : ViewModel() {

    private val queryState = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    val searchScreenState = queryState
        .filter { it.isNotEmpty() }
        .debounce(500)
        .distinctUntilChanged()
        .map { query -> searchPoiUseCase(SearchPoiUseCase.Params(query)) }
        .map { result ->
            if (result.isEmpty()) SearchScreenUiState.NothingFound
            else SearchScreenUiState.SearchResult(result.map { it.toListUiModel() })
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SearchScreenUiState.None
        )

    fun onSearch(query: String) {
        queryState.value = query
    }
}

sealed class SearchScreenUiState {
    object NothingFound : SearchScreenUiState()
    object None : SearchScreenUiState()
    data class SearchResult(val result: List<PoiListItem>) : SearchScreenUiState()
}