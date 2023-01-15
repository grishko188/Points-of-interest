package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.PoiModel
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetDetailedPoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetDetailedPoiUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetDetailedPoiUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetDetailedPoiUseCase invokes getDetailedPoi repo function`() = runTest {
        val id = "someId"
        val model = mock<PoiModel>()
        whenever(repository.getDetailedPoi(anyNonNull())).thenReturn(model)
        val result = SUT.invoke(GetDetailedPoiUseCase.Params(id))
        val captor = argumentCaptor<String>()
        verify(repository, times(1)).getDetailedPoi(capture(captor))

        assertEquals(model, result)
        assertEquals(captor.value, id)
    }

    @Test(expected = Throwable::class)
    fun `test GetDetailedPoiUseCase throws exception when getDetailedPoi throws exception`() = runTest {
        val id = "someId"
        whenever(repository.getDetailedPoi(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(GetDetailedPoiUseCase.Params(id))
    }
}