package com.grishko188.pointofinterest.features.poi.view.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.poi.interactor.*
import com.grishko188.domain.features.poi.models.PoiCommentPayload
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.pointofinterest.features.poi.view.models.PoiDetailListItem
import com.grishko188.pointofinterest.features.poi.view.models.toUIModelWithComments
import com.grishko188.pointofinterest.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewPoiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPoiUseCase: GetDetailedPoiUseCase,
    private val deletePoiUseCase: DeletePoiUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : ViewModel() {

    private val modelState = MutableStateFlow(PoiModel.EMPTY)

    private val _finishScreenState = MutableSharedFlow<Boolean>()
    val finishScreenState = _finishScreenState.asSharedFlow()

    private val _uiState = MutableStateFlow<List<PoiDetailListItem>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _itemToDeleteState = MutableStateFlow<List<String>>(emptyList())
    val itemToDeleteState = _itemToDeleteState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainState = savedStateHandle.getStateFlow(Screen.ViewPoiDetailed.ARG_POI_ID, "")
        .filter { it.isNotEmpty() }
        .map { getDetailedPoiUseCase(GetDetailedPoiUseCase.Params(it)) }
        .onEach { model -> modelState.emit(model) }
        .flatMapLatest { poi ->
            getCommentsUseCase(GetCommentsUseCase.Params(poi.id))
                .onEach { comments -> _uiState.value = poi.toUIModelWithComments(comments) }
                .catch { error -> Log.e(ViewPoiViewModel::class.java.simpleName, error.message, error) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    init {
        mainState.launchIn(viewModelScope)
    }

    fun onAddComment(message: String) {
        viewModelScope.launch {
            val payload = PoiCommentPayload(message)
            addCommentUseCase(AddCommentUseCase.Params(modelState.value.id, payload))
        }
    }

    fun onDeleteComment(id: String) {
        val updatedList = _itemToDeleteState.value.toMutableList()
        updatedList.add(id)
        _itemToDeleteState.value = updatedList
    }

    fun onUndoDeleteComment(id: String) {
        val updatedList = _itemToDeleteState.value.toMutableList()
        updatedList.remove(id)
        _itemToDeleteState.value = updatedList
    }

    fun onCommitCommentDelete(id: String) {
        viewModelScope.launch {
            deleteCommentUseCase(DeleteCommentUseCase.Params(id))
        }
    }

    fun onDeletePoi() {
        viewModelScope.launch {
            deletePoiUseCase(DeletePoiUseCase.Params(modelState.value))
            _finishScreenState.emit(true)
        }
    }
}