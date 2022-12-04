package com.grishko188.pointofinterest.features.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.pointofinterest.core.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun onLaunched() {
        fetchData()
    }

    fun onRetry() {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            delay(2000)
            val random = Random.nextBoolean()
            if (random) {
                _uiState.emit(UiState.Empty)
            } else {
                _uiState.emit(UiState.Error("Failed to fetch data from server"))
            }
        }
    }
}