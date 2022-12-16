package com.grishko188.pointofinterest.features.main.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.domain.features.categories.interactor.SyncCategoriesUseCase
import com.grishko188.domain.features.profile.interactor.GetUserSettingsUseCase
import com.grishko188.domain.features.profile.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _syncState = MutableStateFlow<SyncStateState>(SyncStateState.Loading)
    val syncState = _syncState.asStateFlow()

    val mainScreenState = getUserSettingsUseCase(Unit)
        .map { MainScreenState.Result(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainScreenState.Loading
        )

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch {
            delay(500)
            syncCategoriesUseCase(Unit)
            _syncState.value = SyncStateState.Success
        }
    }
}

sealed class SyncStateState {
    object Loading : SyncStateState()
    object Success : SyncStateState()
}

sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Result(val userSettings: UserSettings) : MainScreenState()
}