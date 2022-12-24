package com.grishko188.data.features.poi.datasource

import android.util.Log
import com.grishko188.data.features.poi.api.WizardServiceApi
import com.grishko188.data.features.poi.model.WizardSuggestionDataModel
import javax.inject.Inject

class WizardRemoteDataSource @Inject constructor(
    private val api: WizardServiceApi
) {

    suspend fun getWizardSuggestion(url: String): WizardSuggestionDataModel {
        val responseBody = api.getUrlContent(url)
        val contentType = responseBody.contentType()?.type
        Log.d("AAA", "Content type $contentType")
        return if (contentType?.contains("image") == true || contentType == "binary")  {
            WizardSuggestionDataModel(contentUrl = url, imageUrl = url)
        } else
            WizardSuggestionDataModel(contentUrl = url, title = "Test", body = "Test body")
    }
}