package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.PoiComment
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
class GetCommentsUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetCommentsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetCommentsUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetCommentsUseCase invokes getComments repo function`() = runTest {
        val targetId = "123"
        val comment1 = mock<PoiComment>()
        val comment2 = mock<PoiComment>()
        val mockResult = arrayListOf(comment1, comment2)
        whenever(repository.getComments(anyNonNull())).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(GetCommentsUseCase.Params(targetId)).first()
        val captor = argumentCaptor<String>()
        verify(repository, times(1)).getComments(capture(captor))
        Assert.assertArrayEquals(mockResult.toTypedArray(), result.toTypedArray())
    }


    @Test
    fun `test GetCommentsUseCase emits exception when flow in getComments throws exception`() = runTest {
        val targetId = "123"
        whenever(repository.getComments(anyNonNull())).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()
        SUT.invoke(GetCommentsUseCase.Params(targetId)).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}