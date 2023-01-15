package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.categories.models.Category
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import com.grishko188.domain.features.poi.models.PoiStatisticsSnapshot
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.Mockito
import org.mockito.Mockito.anyList
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetPoStatisticsUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    @Mock
    private lateinit var categoriesRepository: CategoriesRepository

    private lateinit var SUT: GetPoiStatisticsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetPoiStatisticsUseCase(categoriesRepository, repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetPoiStatisticsUseCase invokes getStatistics repo function`() = runTest {
        val snapshotMock = mock<PoiStatisticsSnapshot>()
        val categoriesUsageFake = hashMapOf("1" to 10, "3" to 20)
        val categoriesFake = listOf(
            Category(id = "1", title = "", color = 1, categoryType = CategoryType.PERSONAL, isMutable = true),
            Category(id = "3", title = "", color = 2, categoryType = CategoryType.PERSONAL, isMutable = true)
        )

        whenever(repository.getStatistics()).thenReturn(snapshotMock)
        whenever(snapshotMock.categoriesUsageCount).thenReturn(categoriesUsageFake)
        whenever(categoriesRepository.getCategories(anyList())).thenReturn(flowOf(categoriesFake))

        SUT.invoke(Unit)

        val captor = argumentCaptor<List<Int>>()

        verify(repository, Mockito.times(1)).getStatistics()
        verify(categoriesRepository, Mockito.times(1)).getCategories(capture(captor))

        Assert.assertArrayEquals(
            categoriesUsageFake.keys.map { it.toInt() }.toTypedArray(),
            captor.value.toTypedArray()
        )
    }

    @Test(expected = Throwable::class)
    fun `test GetPoiStatisticsUseCase throws exception when getStatistics throws exception`() = runTest {
        whenever(repository.getStatistics()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}