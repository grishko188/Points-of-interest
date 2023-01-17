package com.grishko188.pointofinterest.features.home.vm

import androidx.lifecycle.SavedStateHandle
import com.grishko188.domain.features.categories.interactor.GetCategoriesByIdsUseCase
import com.grishko188.domain.features.poi.interactor.GetPoiListUseCase
import com.grishko188.domain.features.poi.interactor.GetUsedCategoriesUseCase
import com.grishko188.pointofinterest.features.home.ui.models.PoiSortByUiOption
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
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    private val savedStateHandle = SavedStateHandle()

    @Inject
    lateinit var getUsedCategoriesUseCase: GetUsedCategoriesUseCase

    @Inject
    lateinit var getCategoriesByIdsUseCase: GetCategoriesByIdsUseCase

    @Inject
    lateinit var getPoiListUseCase: GetPoiListUseCase

    lateinit var SUT: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()

        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = HomeViewModel(
            savedStateHandle,
            getUsedCategoriesUseCase,
            getCategoriesByIdsUseCase,
            getPoiListUseCase
        )
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
        Dispatchers.resetMain()
    }

    @Test
    fun `test HomeViewModel initial homeUiContentState is Loading`() = runTest {
        assertIs<HomeViewModel.HomeUiContentState.Loading>(SUT.homeUiContentState.value)
    }

    @Test
    fun `test HomeViewModel homeUiContentState is Result when loading is finished`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.homeUiContentState.collect() }
        advanceTimeBy(200)
        assertIs<HomeViewModel.HomeUiContentState.Result>(SUT.homeUiContentState.value)
        collectJob.cancel()
    }

    @Test
    fun `test HomeViewModel onApplySortBy updates displaySortOptionUiState and re updated homeUiContentState`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.homeUiContentState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { SUT.displaySortOptionUiState.collect() }
        assertEquals(PoiSortByUiOption.DATE, SUT.displaySortOptionUiState.value)

        SUT.onApplySortBy(PoiSortByUiOption.SEVERITY)

        assertEquals(PoiSortByUiOption.SEVERITY, SUT.displaySortOptionUiState.value)
        assertIs<HomeViewModel.HomeUiContentState.Loading>(SUT.homeUiContentState.value)

        advanceTimeBy(200)
        assertIs<HomeViewModel.HomeUiContentState.Result>(SUT.homeUiContentState.value)
        collectJob.cancel()
        collectJob2.cancel()
    }

    @Test
    fun `test HomeViewModel onRetry re trigger update of homeUiContentState`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.homeUiContentState.collect() }
        assertIs<HomeViewModel.HomeUiContentState.Loading>(SUT.homeUiContentState.value)
        advanceTimeBy(200)
        assertIs<HomeViewModel.HomeUiContentState.Result>(SUT.homeUiContentState.value)

        SUT.onRetry()

        assertIs<HomeViewModel.HomeUiContentState.Loading>(SUT.homeUiContentState.value)
        advanceTimeBy(200)
        assertIs<HomeViewModel.HomeUiContentState.Result>(SUT.homeUiContentState.value)

        collectJob.cancel()
    }

    @Test
    fun `test HomeViewModel initial categoriesState is emptyList`() = runTest {
        assertEquals(emptyList(), SUT.categoriesState.value)
    }

    @Test
    fun `test HomeViewModel categoriesState is reflex used categories`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.categoriesState.collect() }
        assert(SUT.categoriesState.value.isNotEmpty())
        collectJob.cancel()
    }
}