package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.model.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetProfileUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: GetProfileUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetProfileUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetProfileUseCase invokes getUserProfile and getUserSettings repo functions`() = runTest {
        val mockProfileResult = mock<UserProfile>()
        val mockUserSettings = mock<UserSettings>()

        whenever(repository.getUserProfile()).thenReturn(flowOf(mockProfileResult))
        whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
        val result = SUT.invoke(Unit).first()
        verify(repository, Mockito.times(1)).getUserProfile()
        verify(repository, Mockito.times(1)).getUserSetting()
        assertEquals(mockProfileResult, result.userProfile)
        assertEquals(mockUserSettings, result.userSettings)
    }


    @Test
    fun `test GetProfileUseCase emits exception when flow in getUserProfile throws exception`() = runTest {
        val mockUserSettings = mock<UserSettings>()
        whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
        whenever(repository.getUserProfile()).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }

    @Test
    fun `test get profile use case emits exception when flow in getUserSettings throws exception`() = runTest {
        val mockProfileResult = mock<UserProfile>()
        whenever(repository.getUserProfile()).thenReturn(flowOf(mockProfileResult))
        whenever(repository.getUserSetting()).thenReturn(flow { throw IllegalStateException() })
        var exception = Throwable()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}