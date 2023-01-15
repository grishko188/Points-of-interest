package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.PoiCommentPayload
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
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddCommentUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: AddCommentUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = AddCommentUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test AddCommentUseCase invokes addComment repo function`() = runTest {
        val targetPoiId = "123"
        val commentCreationPayload = PoiCommentPayload("message")
        SUT.invoke(AddCommentUseCase.Params(targetPoiId, commentCreationPayload))
        val idCaptor = argumentCaptor<String>()
        val payloadCaptor = argumentCaptor<PoiCommentPayload>()
        verify(repository, Mockito.times(1)).addComment(capture(idCaptor), capture(payloadCaptor))
        assertEquals(payloadCaptor.value, commentCreationPayload)
        assertEquals(idCaptor.value, targetPoiId)
    }

    @Test(expected = Throwable::class)
    fun `test AddCategoryUseCaseTest throws exception when addCategory throws exception`() = runTest {
        val targetPoiId = "123"
        val commentCreationPayload = PoiCommentPayload("message")
        whenever(repository.addComment(anyNonNull(), anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(AddCommentUseCase.Params(targetPoiId, commentCreationPayload))
    }
}