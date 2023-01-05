package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.PoiModel
import com.grishko188.domain.features.poi.models.PoiSortOption
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class GetPoiListUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetPoiListUseCase

    @Before
    fun setup() {
        SUT = GetPoiListUseCase(repository)
    }

    @Test
    fun `test GetPoiListUseCase invokes getPoiList repo function`() = runTest {
        val sortOption = mock<PoiSortOption>()
        val model1 = mock<PoiModel>()
        val model2 = mock<PoiModel>()
        val mockResult = arrayListOf(model1, model2)
        whenever(repository.getPoiList(anyNonNull())).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(GetPoiListUseCase.Params(sortOption)).first()
        val captor = argumentCaptor<PoiSortOption>()
        verify(repository, times(1)).getPoiList(capture(captor))

        assertEquals(sortOption, captor.value)
        Assert.assertArrayEquals(mockResult.toTypedArray(), result.toTypedArray())
    }


    @Test
    fun `test GetPoiListUseCase emits exception when flow in getPoiList throws exception`() = runTest {
        val sortOption = mock<PoiSortOption>()
        whenever(repository.getPoiList(anyNonNull())).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()
        SUT.invoke(GetPoiListUseCase.Params(sortOption)).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}