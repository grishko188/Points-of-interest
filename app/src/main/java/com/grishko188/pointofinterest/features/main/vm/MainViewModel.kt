package com.grishko188.pointofinterest.features.main.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.categories.interactor.SyncCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncCategoriesUseCase: SyncCategoriesUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch {
            syncCategoriesUseCase(Unit)
            _uiState.value = MainUiState.Success
        }
    }
}

sealed class MainUiState {
    object Loading : MainUiState()
    object Success : MainUiState()
}