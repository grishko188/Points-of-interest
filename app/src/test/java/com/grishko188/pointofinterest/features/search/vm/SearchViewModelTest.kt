package com.grishko188.pointofinterest.features.search.vm

import com.grishko188.domain.features.poi.interactor.SearchPoiUseCase
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var searchUseCase: SearchPoiUseCase

    lateinit var SUT: SearchVm

    @Before
    fun setup() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = SearchVm(searchUseCase)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SearchVm searchScreenState is NothingFound when searchUseCase returns empty list`() = runTest {
        assertEquals(SearchScreenUiState.None, SUT.searchScreenState.value)
        val job = launch { SUT.searchScreenState.collect() }
        SUT.onSearch("Query")
        advanceTimeBy(500)
        runCurrent()
        assertEquals(SearchScreenUiState.NothingFound, SUT.searchScreenState.value)
        job.cancel()
    }

    @Test
    fun `test SearchVm searchScreenState is None and searchUseCase is not invoked when search query is empty string`() = runTest {
        assertEquals(SearchScreenUiState.None, SUT.searchScreenState.value)
        val job = launch { SUT.searchScreenState.collect() }
        SUT.onSearch("")
        advanceTimeBy(500)
        runCurrent()
        assertEquals(SearchScreenUiState.None, SUT.searchScreenState.value)
        job.cancel()
    }

    @Test
    fun `test SearchVm searchScreenState is SearchResult when searchUseCase returns result`() = runTest {
        assertEquals(SearchScreenUiState.None, SUT.searchScreenState.value)
        val job = launch { SUT.searchScreenState.collect() }

        SUT.onSearch("Test query")
        advanceTimeBy(500)
        runCurrent()
        assertTrue(SUT.searchScreenState.value is SearchScreenUiState.SearchResult)
        job.cancel()
    }
}