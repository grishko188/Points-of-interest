package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SetOnboardingShownUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetOnboardingShownUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = SetOnboardingShownUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SetOnboardingShownUseCaseTest invokes setShowOnBoarding repo function`() = runTest {
        SUT.invoke(Unit)
        val captor = argumentCaptor<Boolean>()
        verify(repository, times(1)).setShowOnBoarding(capture(captor))
        assertFalse(captor.value)
    }

    @Test(expected = Throwable::class)
    fun `test SetOnboardingShownUseCaseTest throws exception when setShowOnBoarding throws exception`() = runTest {
        MockitoHelper.whenever(repository.setShowOnBoarding(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}