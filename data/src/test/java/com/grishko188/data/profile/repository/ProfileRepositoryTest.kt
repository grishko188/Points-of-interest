package com.grishko188.data.profile.repository

import com.grishko188.data.MockitoHelper.anyNonNull
import com.grishko188.data.MockitoHelper.argumentCaptor
import com.grishko188.data.MockitoHelper.capture
import com.grishko188.data.MockitoHelper.whenever
import com.grishko188.data.features.profile.datasource.ProfileDataSource
import com.grishko188.data.features.profile.model.UserProfileDataModel
import com.grishko188.data.features.profile.model.UserSettingsDataModel
import com.grishko188.data.features.profile.model.toDataModel
import com.grishko188.data.features.profile.model.toDomain
import com.grishko188.data.features.profile.repository.ProfileRepositoryImpl
import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProfileRepositoryTest {

    @Mock
    private lateinit var localDataSource: ProfileDataSource

    private lateinit var SUT: ProfileRepository

    @Before
    fun setup() {
        SUT = ProfileRepositoryImpl(localDataSource)
    }

    @Test
    fun `test getUserProfile function invokes local data source getUserProfile`() = runTest {
        val userProfileStub = UserProfileDataModel("token", "name", "email", "image")
        whenever(localDataSource.getUserProfile()).thenReturn(flowOf(userProfileStub))
        val result = SUT.getUserProfile().first()
        verify(localDataSource, times(1)).getUserProfile()
        assertEquals(userProfileStub.toDomain(), result)
    }

    @Test
    fun `test setUserProfile function invokes local data source setUserProfile`() = runTest {
        val userProfileStub = UserProfile("token", "name", "email", "image")
        whenever(localDataSource.setUserProfile(anyNonNull())).thenReturn(Unit)
        SUT.setUserProfile(userProfileStub)
        val captor = argumentCaptor<UserProfileDataModel>()
        verify(localDataSource, times(1)).setUserProfile(capture(captor))
        assertEquals(userProfileStub.toDataModel(), captor.value)
    }

    @Test
    fun `test deleteUserProfile function invokes local data source deleteUserProfile`() = runTest {
        whenever(localDataSource.deleteUserProfile()).thenReturn(Unit)
        SUT.deleteUserProfile()
        verify(localDataSource, times(1)).deleteUserProfile()
    }

    @Test
    fun `test getUserSettings function invokes local data source getUserSettings`() = runTest {
        val userSettings = UserSettingsDataModel(useSystemTheme = true, useDarkTheme = false, useAutoGc = false, showOnBoarding = false)
        whenever(localDataSource.getUserSettings()).thenReturn(flowOf(userSettings))
        val result = SUT.getUserSetting().first()
        verify(localDataSource, times(1)).getUserSettings()
        assertEquals(userSettings.toDomain(), result)
    }

    @Test
    fun `test setUseSystemTheme function invokes local data source setUseSystemTheme`() = runTest {
        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseSystemTheme(anyNonNull())).thenReturn(Unit)
        SUT.setUseSystemTheme(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseSystemTheme(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun `test setUseDarkTheme function invokes local data source setUseDarkTheme`() = runTest {
        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseDarkTheme(anyNonNull())).thenReturn(Unit)
        SUT.setUseDarkTheme(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseDarkTheme(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun `test setUseAutoGc function invokes local data source setUseAutoGc`() = runTest {
        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseAutoGc(anyNonNull())).thenReturn(Unit)
        SUT.setUseAutoGc(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseAutoGc(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun `test setShowOnBoarding function invokes local data source setShowOnBoarding`() = runTest {
        val argument = Random.nextBoolean()
        whenever(localDataSource.setShowOnBoarding(anyNonNull())).thenReturn(Unit)
        SUT.setShowOnBoarding(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setShowOnBoarding(capture(captor))
        assertEquals(argument, captor.value)
    }
}