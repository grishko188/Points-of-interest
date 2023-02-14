package com.grishko188.data_test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.grishko188.data.core.UserProfile
import com.grishko188.data.core.UserSettings
import com.grishko188.data.di.DatastoreModule
import com.grishko188.data.features.profile.datastore.UserProfileProto
import com.grishko188.data.features.profile.datastore.UserProfileSerializer
import com.grishko188.data.features.profile.datastore.UserSettingsProto
import com.grishko188.data.features.profile.datastore.UserSettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatastoreModule::class]
)
object TestDataStoreModule {

    @Provides
    @Singleton
    @UserSettings
    fun providesUserSettingsDataStore(
        temporaryFolder: TemporaryFolder,
        serializer: UserSettingsSerializer
    ): DataStore<UserSettingsProto> =
        DataStoreFactory.create(serializer = serializer) {
            temporaryFolder.newFile("poi_user_settings.pb")
        }

    @Provides
    @Singleton
    @UserProfile
    fun providesUserProfileDataStore(
        temporaryFolder: TemporaryFolder,
        serializer: UserProfileSerializer
    ): DataStore<UserProfileProto> =
        DataStoreFactory.create(serializer = serializer) {
            temporaryFolder.newFile("poi_user_profile.pb")
        }
}