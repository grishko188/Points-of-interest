package com.grishko188.domain.features.profile.model

sealed class ManualSettings {
    object UseSystemTheme : ManualSettings()
    object UseDarkTheme : ManualSettings()
    object UseAutoGc : ManualSettings()
}