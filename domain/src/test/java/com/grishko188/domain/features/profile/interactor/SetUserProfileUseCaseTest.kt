package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SetUserProfileUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetUserProfileUseCase

    @Before
    fun setup() {
        SUT = SetUserProfileUseCase(repository)
    }

    @Test
    fun `test SetUserProfileUseCaseTest invokes setUserProfile repo function`() = runTest {
        val userProfile = mock<UserProfile>()
        SUT.invoke(SetUserProfileUseCase.Params(userProfile))
        val captor = MockitoHelper.argumentCaptor<UserProfile>()
        Mockito.verify(repository, Mockito.times(1)).setUserProfile(capture(captor))
        assertEquals(captor.value, userProfile)
    }

    @Test(expected = Throwable::class)
    fun `test SetUserProfileUseCaseTest throws exception when addCategory throws exception`() = runTest {
        val userProfile = mock<UserProfile>()
        MockitoHelper.whenever(repository.setUserProfile(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(SetUserProfileUseCase.Params(userProfile))
    }
}