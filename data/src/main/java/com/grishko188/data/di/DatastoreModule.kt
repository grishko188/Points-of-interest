package com.grishko188.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.grishko188.data.core.UserProfile
import com.grishko188.data.core.UserSettings
import com.grishko188.data.features.profile.datastore.UserProfileSerializer
import com.grishko188.data.features.profile.datastore.UserSettingsSerializer
import com.grishko188.data.features.profile.datastore.UserProfileProto
import com.grishko188.data.features.profile.datastore.UserSettingsProto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    @UserSettings
    fun providesUserSettingsDataStore(
        @ApplicationContext context: Context,
        serializer: UserSettingsSerializer
    ): DataStore<UserSettingsProto> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        ) {
            context.dataStoreFile("poi_user_settings.pb")
        }

    @Provides
    @Singleton
    @UserProfile
    fun providesUserProfileDataStore(
        @ApplicationContext context: Context,
        serializer: UserProfileSerializer
    ): DataStore<UserProfileProto> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        ) {
            context.dataStoreFile("poi_user_profile.pb")
        }
}