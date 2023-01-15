package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.PoiCreationPayload
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
class CreatePoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: CreatePoiUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = CreatePoiUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test CreatePoiUseCase invokes createPoi repo function`() = runTest {
        val payload = PoiCreationPayload("link", "title", "body", "imageUrl", emptyList())
        SUT.invoke(CreatePoiUseCase.Params(payload))
        val captor = argumentCaptor<PoiCreationPayload>()
        verify(repository, times(1)).createPoi(capture(captor))
        assertEquals(captor.value, payload)
    }

    @Test(expected = Throwable::class)
    fun `test CreatePoiUseCase throws exception when createPoi throws exception`() = runTest {
        val payload = PoiCreationPayload("link", "title", "body", "imageUrl", emptyList())
        whenever(repository.createPoi(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(CreatePoiUseCase.Params(payload))
    }
}