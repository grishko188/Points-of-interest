package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCategoriesUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: GetCategoriesUseCase

    @Before
    fun setup() {
        SUT = GetCategoriesUseCase(repository)
    }

    @Test
    fun `test GetCategoriesUseCase invokes getCategories repo function`() = runTest {
        val mockCategory1 = mock<Category>()
        val mockCategory2 = mock<Category>()
        val mockResult = arrayListOf(mockCategory1, mockCategory2)
        whenever(repository.getCategories()).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(Unit).first()
        Mockito.verify(repository, Mockito.times(1)).getCategories()
        Assert.assertArrayEquals(mockResult.toTypedArray(), result.toTypedArray())
    }


    @Test
    fun `test GetCategoriesUseCase emits exception when flow in getCategories throws exception`() = runTest {
        whenever(repository.getCategories()).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}