package com.grishko188.data_test.doubles

import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.model.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val userProfileState = MutableSharedFlow<UserProfile>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val userSettingsState = MutableSharedFlow<UserSettings>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        userProfileState.tryEmit(emptyUserData)
        userSettingsState.tryEmit(defaultUserSettings)
    }

    override fun getUserProfile(): Flow<UserProfile> = userProfileState

    override suspend fun setUserProfile(userProfile: UserProfile) {
        userProfileState.tryEmit(userProfile)
    }

    override suspend fun deleteUserProfile() {
        TODO("Not yet implemented")
    }

    override fun getUserSetting(): Flow<UserSettings> = userSettingsState

    override suspend fun setUseSystemTheme(state: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setUseDarkTheme(state: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setUseAutoGc(state: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setShowOnBoarding(state: Boolean) {
        TODO("Not yet implemented")
    }
}