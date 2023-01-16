package com.grishko188.pointofinterest.features.main.vm

import androidx.lifecycle.LifecycleOwner
import com.grishko188.domain.features.categories.interactor.SyncCategoriesUseCase
import com.grishko188.domain.features.profile.interactor.GetUserSettingsUseCase
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Inject
    lateinit var getUserSettingsUseCase: GetUserSettingsUseCase

    @Inject
    lateinit var syncCategoriesUseCase: SyncCategoriesUseCase

    lateinit var SUT: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()

        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = MainViewModel(
            getUserSettingsUseCase,
            syncCategoriesUseCase
        )
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
        Dispatchers.resetMain()
    }

    @Test
    fun `test MainViewModel initial syncState is Loading`() = runTest {
        assertIs<SyncState.Loading>(SUT.syncState.value)
    }

    @Test
    fun `test MainViewModel syncState is Success after onCreate is called`() = runTest {
        assertIs<SyncState.Loading>(SUT.syncState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.syncState.collect() }
        SUT.onCreate(lifecycleOwner)
        runCurrent()
        advanceTimeBy(600)
        assertIs<SyncState.Success>(SUT.syncState.value)
        collectJob.cancel()
    }

    @Test
    fun `test MainViewModel initial mainScreenState is Loading`() = runTest {
        assertIs<MainScreenState.Loading>(SUT.mainScreenState.value)
    }

    @Test
    fun `test MainViewModel mainScreenState is Result after collecting user settings`() = runTest {
        assertIs<MainScreenState.Loading>(SUT.mainScreenState.value)
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.mainScreenState.collect() }
        runCurrent()
        assertIs<MainScreenState.Result>(SUT.mainScreenState.value)
        collectJob.cancel()
    }
}