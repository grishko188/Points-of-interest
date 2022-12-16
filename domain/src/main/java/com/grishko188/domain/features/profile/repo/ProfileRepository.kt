package com.grishko188.domain.features.profile.repo

import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUserProfile(): Flow<UserProfile>
    suspend fun setUserProfile(userProfile: UserProfile)
    suspend fun deleteUserProfile()

    fun getUserSetting(): Flow<UserSettings>
    suspend fun setUseSystemTheme(state: Boolean)
    suspend fun setUseDarkTheme(state: Boolean)
    suspend fun setUseAutoGc(state: Boolean)
    suspend fun setShowOnBoarding(state: Boolean)
}