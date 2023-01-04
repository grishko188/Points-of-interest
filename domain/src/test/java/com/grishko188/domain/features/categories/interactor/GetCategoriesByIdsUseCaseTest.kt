package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
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
class GetCategoriesByIdsUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository


    private lateinit var SUT: GetCategoriesByIdsUseCase

    @Before
    fun setup() {
        SUT = GetCategoriesByIdsUseCase(repository)
    }

    @Test
    fun `test get categories by ids use case invokes getCategories repo function`() = runTest {
        val ids = arrayListOf(1, 2, 3)

        val mockCategory1 = mock<Category>()
        val mockCategory2 = mock<Category>()
        val mockCategory3 = mock<Category>()

        val mockResult = arrayListOf(mockCategory1, mockCategory2, mockCategory3)

        MockitoHelper.whenever(repository.getCategories(ids)).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(GetCategoriesByIdsUseCase.Params(ids)).first()
        val captor = argumentCaptor<List<Int>>()
        Mockito.verify(repository, Mockito.times(1)).getCategories(capture(captor))
        Assert.assertArrayEquals(ids.toTypedArray(), captor.value.toTypedArray())
        Assert.assertArrayEquals(mockResult.toTypedArray(), result.toTypedArray())
    }


    @Test
    fun `test get categories by ids use case emits exception when flow in getCategories throws exception`() = runTest {
        val ids = arrayListOf(1, 2, 3)
        MockitoHelper.whenever(repository.getCategories(ids)).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()
        SUT.invoke(GetCategoriesByIdsUseCase.Params(ids)).catch { exception = it }.toList()
        assertEquals(IllegalStateException::class.java, exception::class.java)
    }
}