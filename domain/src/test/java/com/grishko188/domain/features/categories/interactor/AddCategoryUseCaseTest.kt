package com.grishko188.domain.features.categories.interactor

import android.graphics.Color
import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.categories.models.CreateCategoryPayload
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddCategoryUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: AddCategoryUseCase

    @Before
    fun setup() {
        SUT = AddCategoryUseCase(repository)
    }

    @Test
    fun `test AddCategoryUseCaseTest invokes addCategory repo function`() = runTest {
        val categoryName = "Random name"
        val color = Color.WHITE
        SUT.invoke(AddCategoryUseCase.Params(categoryName, color))
        val payloadCapture = argumentCaptor<CreateCategoryPayload>()
        verify(repository, times(1)).addCategory(capture(payloadCapture))
        assertEquals(payloadCapture.value, CreateCategoryPayload(categoryName, color))
    }

    @Test(expected = Throwable::class)
    fun `test AddCategoryUseCaseTest throws exception when addCategory throws exception`() = runTest {
        val categoryName = "Random name"
        val color = Color.WHITE
        whenever(repository.addCategory(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(AddCategoryUseCase.Params(categoryName, color))
    }
}