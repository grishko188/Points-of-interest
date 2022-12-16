package com.grishko188.data.features.profile.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import com.grishko188.data.core.UserProfile
import com.grishko188.data.core.UserSettings
import com.grishko188.data.features.profile.datastore.UserProfileProto
import com.grishko188.data.features.profile.datastore.UserSettingsProto
import com.grishko188.data.features.profile.datastore.copy
import com.grishko188.data.features.profile.model.UserProfileDataModel
import com.grishko188.data.features.profile.model.UserSettingsDataModel
import com.grishko188.data.features.profile.model.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ProfileLocalDataSource @Inject constructor(
    @UserProfile private val userProfileDataStore: DataStore<UserProfileProto>,
    @UserSettings private val userSettingsDataStore: DataStore<UserSettingsProto>
) : ProfileDataSource {

    override fun getUserProfile(): Flow<UserProfileDataModel> =
        userProfileDataStore.data.map { it.toDataModel() }

    override suspend fun setUserProfile(userProfileDataModel: UserProfileDataModel) {
        try {
            userProfileDataStore.updateData {
                it.copy {
                    authToken = userProfileDataModel.token ?: ""
                    name = userProfileDataModel.name ?: ""
                    email = userProfileDataModel.email ?: ""
                    profileImage = userProfileDataModel.profileImage ?: ""
                }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to update user profile", ioException)
        }
    }

    override suspend fun deleteUserProfile() {
        try {
            userProfileDataStore.updateData {
                it.copy {
                    authToken = ""
                    name = ""
                    email = ""
                    profileImage = ""
                }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to delete user profile", ioException)
        }
    }

    override fun getUserSettings(): Flow<UserSettingsDataModel> =
        userSettingsDataStore.data.map { it.toDataModel() }


    override suspend fun setShowOnBoarding(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.copy { onboardingWasShown = state.not() }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setShowOnBoarding", ioException)
        }
    }

    override suspend fun setUseSystemTheme(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.copy { useCustomTheme = state.not() }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseSystemTheme", ioException)
        }
    }

    override suspend fun setUseDarkTheme(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.copy { useDarkTheme = state }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseDarkTheme", ioException)
        }
    }

    override suspend fun setUseAutoGc(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.copy { useAutoGc = state }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseAutoGc", ioException)
        }
    }

}