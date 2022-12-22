package com.grishko188.pointofinterest.features.poi.vm

import androidx.lifecycle.ViewModel
import com.grishko188.domain.features.poi.interactor.CreatePoiUseCase
import com.grishko188.domain.features.poi.interactor.GetWizardSuggestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PoiViewModel @Inject constructor(
    private val getWizardSuggestionUseCase: GetWizardSuggestionUseCase,
    private val createPoiUseCase: CreatePoiUseCase
) : ViewModel() {
}