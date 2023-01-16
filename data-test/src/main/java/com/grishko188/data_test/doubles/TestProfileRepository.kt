package com.grishko188.data_test.doubles

import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.model.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

val emptyUserData = UserProfile(
    authToken = "",
    name = "",
    email = "",
    image = ""
)

val defaultUserSettings = UserSettings(
    isUseSystemTheme = true,
    isDarkMode = false,
    isAutoGcEnabled = false,
    isShowOnBoarding = false
)

class TestProfileRepository @Inject constructor() : ProfileRepository {

    private val userProfileState = MutableStateFlow(emptyUserData)
    private val userSettingsState = MutableStateFlow(defaultUserSettings)

    override fun getUserProfile(): Flow<UserProfile> = userProfileState

    override suspend fun setUserProfile(userProfile: UserProfile) {
        userProfileState.tryEmit(userProfile)
    }

    override suspend fun deleteUserProfile() {
        userProfileState.tryEmit(emptyUserData)
    }

    override fun getUserSetting(): Flow<UserSettings> = userSettingsState

    override suspend fun setUseSystemTheme(state: Boolean) {
        val updated = userSettingsState.value.copy(isUseSystemTheme = state)
        userSettingsState.tryEmit(updated)
    }

    override suspend fun setUseDarkTheme(state: Boolean) {
        val updated = userSettingsState.value.copy(isDarkMode = state)
        userSettingsState.tryEmit(updated)
    }

    override suspend fun setUseAutoGc(state: Boolean) {
        val updated = userSettingsState.value.copy(isAutoGcEnabled = state)
        userSettingsState.tryEmit(updated)
    }

    override suspend fun setShowOnBoarding(state: Boolean) {
        val updated = userSettingsState.value.copy(isShowOnBoarding = state)
        userSettingsState.tryEmit(updated)
    }
}