package com.grishko188.data.poi.model

import com.grishko188.data.features.poi.model.WizardSuggestionDataModel
import com.grishko188.data.features.poi.model.toDomain
import org.junit.Test
import kotlin.test.assertEquals

class WizardSuggestionDataModelTest {

    @Test
    fun `test WizardSuggestionDataModel_toDomain() function returns WizardSuggestion model with correct fields`() {
        val dataModel = WizardSuggestionDataModel(
            contentUrl = "https://www.google.com",
            title = "Title",
            body = "Suggestion body",
            imageUrl = "https://www.google.com/image"
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.contentUrl, domainModel.contentUrl)
        assertEquals(dataModel.title, domainModel.title)
        assertEquals(dataModel.body, domainModel.body)
        assertEquals(dataModel.imageUrl, domainModel.imageUrl)
        assertEquals(null, domainModel.tags)
    }
}