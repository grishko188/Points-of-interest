package com.grishko188.data.profile.model

import com.grishko188.data.features.profile.datastore.UserSettingsProto
import com.grishko188.data.features.profile.model.UserSettingsDataModel
import com.grishko188.data.features.profile.model.toDataModel
import com.grishko188.data.features.profile.model.toDomain
import org.junit.Test
import kotlin.test.assertEquals

class UserSettingsDataModelTest {

    @Test
    fun `test UserSettingsDataModel_toDomain() function returns UserSettings model with correct fields`() {
        val dataModel = UserSettingsDataModel(
            useSystemTheme = false,
            useDarkTheme = true,
            useAutoGc = false,
            showOnBoarding = false
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.useSystemTheme, domainModel.isUseSystemTheme)
        assertEquals(dataModel.useDarkTheme, domainModel.isDarkMode)
        assertEquals(dataModel.useAutoGc, domainModel.isAutoGcEnabled)
        assertEquals(dataModel.showOnBoarding, domainModel.isShowOnBoarding)
    }

    @Test
    fun `test UserSettingsProto_toDataModel() function returns UserSettingsDataModel model with correct fields`() {
        val userSettingsProto = UserSettingsProto.newBuilder().apply {
            useCustomTheme = false
            useDarkTheme = false
            useAutoGc = false
            onboardingWasShown = false
        }.build()

        val dataModel = userSettingsProto.toDataModel()

        assertEquals(dataModel.useSystemTheme, userSettingsProto.useCustomTheme.not())
        assertEquals(dataModel.useDarkTheme, userSettingsProto.useDarkTheme)
        assertEquals(dataModel.useAutoGc, userSettingsProto.useAutoGc)
        assertEquals(dataModel.showOnBoarding, userSettingsProto.onboardingWasShown.not())
    }
}