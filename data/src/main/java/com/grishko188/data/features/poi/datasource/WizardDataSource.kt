package com.grishko188.data.features.poi.datasource

import com.grishko188.data.features.poi.model.WizardSuggestionDataModel

interface WizardDataSource {

    suspend fun getWizardSuggestion(url: String): WizardSuggestionDataModel
}