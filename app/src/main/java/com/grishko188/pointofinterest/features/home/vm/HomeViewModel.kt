package com.grishko188.pointofinterest.features.home.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.categories.interactor.GetCategoriesByIdsUseCase
import com.grishko188.domain.features.poi.interactor.GetPoiListUseCase
import com.grishko188.domain.features.poi.interactor.GetUsedCategoriesUseCase
import com.grishko188.pointofinterest.core.utils.ErrorDisplayObject
import com.grishko188.pointofinterest.core.utils.RetryTrigger
import com.grishko188.pointofinterest.core.utils.retryableFlow
import com.grishko188.pointofinterest.core.utils.toDisplayObject
import com.grishko188.pointofinterest.features.categories.ui.models.toUiModel
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.features.home.ui.models.PoiSortByUiOption
import com.grishko188.pointofinterest.features.home.ui.models.toDomain
import com.grishko188.pointofinterest.features.home.ui.models.toListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getUsedCategoriesUseCase: GetUsedCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesByIdsUseCase,
    private val getPoiListUseCase: GetPoiListUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesState = getUsedCategoriesUseCase(Unit).flatMapLatest { ids ->
        getCategoriesUseCase(GetCategoriesByIdsUseCase.Params(ids))
            .map { list -> list.map { it.toUiModel() } }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _homeUiContentState = MutableStateFlow<HomeUiContentState>(HomeUiContentState.Loading)
    val homeUiContentState = _homeUiContentState.asStateFlow()

    private val _displaySortOptionUiState = MutableStateFlow(PoiSortByUiOption.DATE)
    val displaySortOptionUiState = _displaySortOptionUiState.asStateFlow()

    private val retryTrigger by lazy { RetryTrigger() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainPoiState =
        savedStateHandle.getStateFlow(KEY_SORT_OPTION, PoiSortByUiOption.DATE)
            .onEach { _displaySortOptionUiState.value = it }
            .flatMapLatest { sortOption ->
                retryableFlow(retryTrigger) {
                    getPoiListUseCase(GetPoiListUseCase.Params(sortOption.toDomain()))
                        .map { list -> list.map { it.toListUiModel() } }
                        .onEach {
                            if (it.isEmpty()) _homeUiContentState.value = HomeUiContentState.Empty
                            else _homeUiContentState.value = HomeUiContentState.Result(it)
                        }
                        .catch { _homeUiContentState.value = HomeUiContentState.Error(it.toDisplayObject()) }
                        .onStart { _homeUiContentState.value = HomeUiContentState.Loading }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PoiSortByUiOption.DATE
            )

    init {
        mainPoiState.launchIn(viewModelScope)
    }

    fun onRetry() {
        retryTrigger.retry()
    }

    fun onApplySortBy(option: PoiSortByUiOption) {
        savedStateHandle[KEY_SORT_OPTION] = option
    }

    sealed class HomeUiContentState {
        object Loading : HomeUiContentState()
        object Empty : HomeUiContentState()
        data class Result(val poiList: List<PoiListItem>) : HomeUiContentState()
        data class Error(val displayObject: ErrorDisplayObject) : HomeUiContentState()
    }

    companion object {
        private const val KEY_SORT_OPTION = "sort_option"
    }
}