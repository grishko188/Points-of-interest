package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.MockitoHelper
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
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeleteUserProfileUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: DeleteUserProfileUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = DeleteUserProfileUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test DeleteUserProfileUseCase invokes deleteUseProfile repo function`() = runTest {
        SUT.invoke(Unit)
        Mockito.verify(repository, times(1)).deleteUserProfile()
    }

    @Test(expected = Throwable::class)
    fun `test DeleteUserProfileUseCase throws exception when deleteCategory throws exception`() = runTest {
        MockitoHelper.whenever(repository.deleteUserProfile()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}