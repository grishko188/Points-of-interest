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
class DeletePoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: DeletePoiUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = DeletePoiUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test DeletePoiUseCase invokes deletePoi repo function`() = runTest {
        val model = mock<PoiModel>()
        SUT.invoke(DeletePoiUseCase.Params(model))
        val captor = argumentCaptor<PoiModel>()
        verify(repository, times(1)).deletePoi(capture(captor))
        assertEquals(captor.value, model)
    }

    @Test(expected = Throwable::class)
    fun `test DeletePoiUseCase throws exception when deletePoi throws exception`() = runTest {
        val model = mock<PoiModel>()
        whenever(repository.deletePoi(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(DeletePoiUseCase.Params(model))
    }
}