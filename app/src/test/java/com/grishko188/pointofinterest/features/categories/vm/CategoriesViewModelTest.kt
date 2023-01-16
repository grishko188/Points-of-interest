package com.grishko188.pointofinterest.features.categories.vm

import androidx.compose.ui.graphics.Color
import com.grishko188.data_test.doubles.testCategories
import com.grishko188.domain.features.categories.interactor.*
import com.grishko188.pointofinterest.features.categories.ui.models.DetailedCategoriesUiState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class CategoriesViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Inject
    lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Inject
    lateinit var getCategoryUseCase: GetCategoryUseCase

    @Inject
    lateinit var deleteCategoryUseCase: DeleteCategoryUseCase

    @Inject
    lateinit var updateCategoryUseCase: UpdateCategoryUseCase

    @Inject
    lateinit var addCategoryUseCase: AddCategoryUseCase

    lateinit var SUT: CategoriesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()

        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = CategoriesViewModel(
            getCategoriesUseCase,
            getCategoryUseCase,
            deleteCategoryUseCase,
            updateCategoryUseCase,
            addCategoryUseCase
        )
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
        Dispatchers.resetMain()
    }

    @Test
    fun `test CategoriesViewModel initial categoriesState is emptyMap `() = runTest {
        assertEquals(emptyMap(), SUT.categoriesState.value)
    }

    @Test
    fun `test CategoriesViewModel categoriesState is not empty when start to collect `() = runTest {
        assertEquals(emptyMap(), SUT.categoriesState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.categoriesState.collect() }
        val initialCategories = SUT.categoriesState.value
        assertNotEquals(emptyMap(), initialCategories)
        assertEquals(testCategories.groupBy { it.categoryType }.size, initialCategories.size)
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel onDeleteItem adds id to itemsToDelete state `() = runTest {
        assertEquals(emptyList(), SUT.itemsToDelete.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.itemsToDelete.collect() }
        SUT.onDeleteItem("1")
        val itemsToDelete = SUT.itemsToDelete.value
        assertEquals(1, itemsToDelete.size)
        assert(itemsToDelete.contains("1"))
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel onUndoDeleteItem removes id from itemsToDelete state `() = runTest {
        assertEquals(emptyList(), SUT.itemsToDelete.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.itemsToDelete.collect() }
        SUT.onDeleteItem("1")
        assert(SUT.itemsToDelete.value.contains("1"))

        SUT.onUndoDeleteItem("1")
        assertEquals(0, SUT.itemsToDelete.value.size)
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel onCreateItem updates categoriesState with new item in list`() = runTest {
        assertEquals(emptyMap(), SUT.categoriesState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.categoriesState.collect() }
        val initialCategories = SUT.categoriesState.value
        val expectedInitialCount = testCategories.groupBy { it.categoryType }.values.flatten().size
        assertEquals(expectedInitialCount, initialCategories.values.flatten().size)
        SUT.onCreateItem("New test", Color.Magenta)
        runCurrent()
        assertEquals(expectedInitialCount + 1, SUT.categoriesState.value.values.flatten().size)
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel onCommitDeleteItem updates categoriesState with one item been removed`() = runTest {
        assertEquals(emptyMap(), SUT.categoriesState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.categoriesState.collect() }
        val initialCategories = SUT.categoriesState.value
        val expectedInitialCount = testCategories.groupBy { it.categoryType }.values.flatten().size
        assertEquals(expectedInitialCount, initialCategories.values.flatten().size)
        assert(SUT.categoriesState.value.values.flatten().any { it.id == "2" })

        SUT.onCommitDeleteItem("2")

        runCurrent()
        assertEquals(expectedInitialCount - 1, SUT.categoriesState.value.values.flatten().size)
        assert(SUT.categoriesState.value.values.flatten().none { it.id == "2" })
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel onUpdateItem updates categoriesState with one item been updated`() = runTest {
        assertEquals(emptyMap(), SUT.categoriesState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.categoriesState.collect() }
        val initialCategories = SUT.categoriesState.value
        val itemToUpdate = initialCategories.values.flatten().find { it.id == "1" }!!

        SUT.onUpdateItem(itemToUpdate, "Test Updated Title", itemToUpdate.color)

        runCurrent()
        assertEquals("Test Updated Title", SUT.categoriesState.value.values.flatten().find { it.id == "1" }?.title)
        collectJob.cancel()
    }

    @Test
    fun `test CategoriesViewModel initial detailedCategoriesUiState is DetailedCategoriesUiState_Loading`() = runTest {
        assertIs<DetailedCategoriesUiState.Loading>(SUT.detailedCategoriesUiState.value)
    }

    @Test
    fun `test CategoriesViewModel detailedCategoriesUiState is DetailedCategoriesUiState_Success with null data when onFetchDetailedState called with null categoryId`() =
        runTest {
            assertIs<DetailedCategoriesUiState.Loading>(SUT.detailedCategoriesUiState.value)
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.detailedCategoriesUiState.collect() }
            SUT.onFetchDetailedState(null)
            runCurrent()
            val detailedState = SUT.detailedCategoriesUiState.value
            assertIs<DetailedCategoriesUiState.Success>(detailedState)
            assertNull(detailedState.categoryUiModel)
            collectJob.cancel()
        }

    @Test
    fun `test CategoriesViewModel detailedCategoriesUiState is DetailedCategoriesUiState_Success with not-null data when onFetchDetailedState called with existing categoryId`() =
        runTest {
            assertIs<DetailedCategoriesUiState.Loading>(SUT.detailedCategoriesUiState.value)
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.detailedCategoriesUiState.collect() }
            SUT.onFetchDetailedState("3")
            runCurrent()
            val detailedState = SUT.detailedCategoriesUiState.value
            assertIs<DetailedCategoriesUiState.Success>(detailedState)
            assertNotNull(detailedState.categoryUiModel)
            assertEquals("3", detailedState.categoryUiModel?.id)
            collectJob.cancel()
        }
}