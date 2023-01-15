package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.repo.PoiRepository
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
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeleteGarbageUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: DeleteGarbageUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = DeleteGarbageUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test DeleteGarbageUseCase invokes deleteGarbage repo function`() = runTest {
        SUT.invoke(Unit)
        verify(repository, Mockito.times(1)).deleteGarbage()
    }

    @Test(expected = Throwable::class)
    fun `test DeleteGarbageUseCase throws exception when deleteGarbage throws exception`() = runTest {
        whenever(repository.deleteGarbage()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}