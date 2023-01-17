package com.grishko188.pointofinterest.features.poi.view.vm

import androidx.lifecycle.SavedStateHandle
import com.grishko188.domain.features.poi.interactor.*
import com.grishko188.pointofinterest.navigation.Screen
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class ViewPoiViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    lateinit var SUT: ViewPoiViewModel

    private val savedStateHandle = SavedStateHandle()

    @Inject
    lateinit var getDetailedPoiUseCase: GetDetailedPoiUseCase

    @Inject
    lateinit var getCommentsUseCase: GetCommentsUseCase

    @Inject
    lateinit var deletePoiUseCase: DeletePoiUseCase

    @Inject
    lateinit var addCommentUseCase: AddCommentUseCase

    @Inject
    lateinit var deleteCommentUseCase: DeleteCommentUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = ViewPoiViewModel(
            savedStateHandle,
            getDetailedPoiUseCase,
            deletePoiUseCase,
            getCommentsUseCase,
            addCommentUseCase,
            deleteCommentUseCase
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test ViewPoiViewModel initial uiState is emptyList`() = runTest {
        assertEquals(emptyList(), SUT.uiState.value)
    }

    @Test
    fun `test ViewPoiViewModel uiState is list of PoiDetailListItem when poi id is in SavedStateHandle`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.uiState.collect() }
        assertEquals(emptyList(), SUT.uiState.value)
        savedStateHandle[Screen.ViewPoiDetailed.ARG_POI_ID] = "1"
        runCurrent()
        assert(SUT.uiState.value.isNotEmpty())
        collectJob.cancel()
    }

    @Test
    fun `test ViewPoiViewModel onAddComment updates uiState and value is list with one new item`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.uiState.collect() }
        savedStateHandle[Screen.ViewPoiDetailed.ARG_POI_ID] = "1"
        val initialCount = SUT.uiState.value.size
        SUT.onAddComment("New comment")
        runCurrent()
        assertEquals(initialCount + 1, SUT.uiState.value.size)
        collectJob.cancel()
    }

    @Test
    fun `test ViewPoiViewModel onCommitCommentDelete updates uiState and value is list with one less item`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.uiState.collect() }
        savedStateHandle[Screen.ViewPoiDetailed.ARG_POI_ID] = "1"
        val initialCount = SUT.uiState.value.size
        SUT.onCommitCommentDelete("2")
        runCurrent()
        assertEquals(initialCount - 1, SUT.uiState.value.size)
        collectJob.cancel()
    }

    @Test
    fun `test ViewPoiViewModel onDeleteComment updates itemToDeleteState with new element`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.itemToDeleteState.collect() }
        SUT.onDeleteComment("2")
        assertTrue(SUT.itemToDeleteState.value.contains("2"))
        collectJob.cancel()
    }

    @Test
    fun `test ViewPoiViewModel onUndoDeleteComment updates itemToDeleteState with removing element`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.itemToDeleteState.collect() }
        SUT.onDeleteComment("2")
        assertTrue(SUT.itemToDeleteState.value.contains("2"))
        SUT.onUndoDeleteComment("2")
        assertFalse(SUT.itemToDeleteState.value.contains("2"))
        collectJob.cancel()
    }

    @Test
    fun `test ViewPoiViewModel finishScreenState is emit true when onDeletePoi invoked`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.finishScreenState.collect() }
        assertFalse(SUT.finishScreenState.replayCache.firstOrNull() ?: false)
        savedStateHandle[Screen.ViewPoiDetailed.ARG_POI_ID] = "1"
        runCurrent()
        SUT.onDeletePoi()
        runCurrent()
        assertTrue(SUT.finishScreenState.replayCache.first())
        collectJob.cancel()
    }
}