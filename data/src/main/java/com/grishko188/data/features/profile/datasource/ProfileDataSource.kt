package com.grishko188.data.features.profile.datasource

import com.grishko188.data.features.profile.model.UserProfileDataModel
import com.grishko188.data.features.profile.model.UserSettingsDataModel
import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {

    fun getUserProfile(): Flow<UserProfileDataModel>
    suspend fun setUserProfile(userProfileDataModel: UserProfileDataModel)
    suspend fun deleteUserProfile()

    fun getUserSettings(): Flow<UserSettingsDataModel>
    suspend fun setShowOnBoarding(state: Boolean)
    suspend fun setUseSystemTheme(state: Boolean)
    suspend fun setUseDarkTheme(state: Boolean)
    suspend fun setUseAutoGc(state: Boolean)
}