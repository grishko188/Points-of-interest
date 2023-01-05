package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.profile.model.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetUserSettingsUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: GetUserSettingsUseCase

    @Before
    fun setup() {
        SUT = GetUserSettingsUseCase(repository)
    }

    @Test
    fun `test GetUserSettingsUseCase invokes getUserSettings repo functions`() = runTest {
        val mockUserSettings = MockitoHelper.mock<UserSettings>()
        whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
        val result = SUT.invoke(Unit).first()
        Mockito.verify(repository, Mockito.times(1)).getUserSetting()
        assertEquals(mockUserSettings, result)
    }

    @Test
    fun `test GetUserSettingsUseCase emits exception when flow in getUserSettings throws exception`() = runTest {
        whenever(repository.getUserSetting()).thenReturn(flow { throw IllegalStateException() })
        var exception = Throwable()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}