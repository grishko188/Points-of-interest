package com.grishko188.data.features.profile.model

import com.grishko188.data.features.profile.datastore.UserSettingsProto
import com.grishko188.domain.features.profile.model.UserSettings

data class UserSettingsDataModel(
    val useSystemTheme: Boolean,
    val useDarkTheme: Boolean,
    val useAutoGc: Boolean,
    val showOnBoarding: Boolean
)

fun UserSettingsProto.toDataModel() = UserSettingsDataModel(
    useSystemTheme = useCustomTheme.not(),
    useDarkTheme = useDarkTheme,
    useAutoGc = useAutoGc,
    showOnBoarding = onboardingWasShown.not()
)

fun UserSettingsDataModel.toDomain() = UserSettings(
    isUseSystemTheme = useSystemTheme,
    isDarkMode = useDarkTheme,
    isAutoGcEnabled = useAutoGc,
    isShowOnBoarding = showOnBoarding
)

