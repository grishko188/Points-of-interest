package com.grishko188.pointofinterest.features.poi.create.vm

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.grishko188.domain.features.categories.interactor.GetCategoriesUseCase
import com.grishko188.domain.features.poi.interactor.CreatePoiUseCase
import com.grishko188.domain.features.poi.interactor.GetWizardSuggestionUseCase
import com.grishko188.domain.features.poi.models.WizardSuggestion
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.poi.create.models.ContentType
import com.grishko188.pointofinterest.features.poi.create.models.FormImageState
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class CreatePoiViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    lateinit var SUT: CreatePoiViewModel

    private val savedStateHandle = SavedStateHandle()

    @Mock
    lateinit var createPoiUseCase: CreatePoiUseCase

    @Inject
    lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Mock
    lateinit var getWizardSuggestionUseCase: GetWizardSuggestionUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()

        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = CreatePoiViewModel(
            savedStateHandle,
            getWizardSuggestionUseCase,
            createPoiUseCase,
            getCategoriesUseCase
        )
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
        Dispatchers.resetMain()
    }

    @Test
    fun `test CreatePoiViewModel screenState is Wizard with SharedContent type MANUAL when savedStateHandle do not contains intent`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            assertIs<CreatePoiScreenState.Wizard>(SUT.screenState.value)
            assertNull((SUT.screenState.value as CreatePoiScreenState.Wizard).sharedContent.content)
            assertEquals(ContentType.MANUAL, (SUT.screenState.value as CreatePoiScreenState.Wizard).sharedContent.contentType)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState is Wizard with SharedContent type URL when savedStateHandle contains intent with ACTION_SEND, mimeType is text and web url in extras`() =
        runTest {
            val testUrl = "https://www.test.com"
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            val testIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/any"
                putExtra(Intent.EXTRA_TEXT, testUrl)
            }
            savedStateHandle[NavController.KEY_DEEP_LINK_INTENT] = testIntent

            assertIs<CreatePoiScreenState.Wizard>(SUT.screenState.value)
            assertEquals(testUrl, (SUT.screenState.value as CreatePoiScreenState.Wizard).sharedContent.content)
            assertEquals(ContentType.URL, (SUT.screenState.value as CreatePoiScreenState.Wizard).sharedContent.contentType)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState is Form with WizardSuggestionUiModel data and body is not null when savedStateHandle contains intent with ACTION_SEND, mimeType is text and plain in extras`() =
        runTest {
            val testText = "Plain text"
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            val testIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/any"
                putExtra(Intent.EXTRA_TEXT, testText)
            }
            savedStateHandle[NavController.KEY_DEEP_LINK_INTENT] = testIntent

            assertIs<CreatePoiScreenState.Form>(SUT.screenState.value)
            assertEquals(testText, (SUT.screenState.value as CreatePoiScreenState.Form).suggestion.body)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState is Form with WizardSuggestionUiModel data and imageUrl is not null when savedStateHandle contains intent with ACTION_SEND, mimeType is image and image uri is in extras`() =
        runTest {
            val testUri = Uri.parse("content:///files/somedir/image.png")
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            val testIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/any"
                putExtra(Intent.EXTRA_STREAM, testUri)
            }
            savedStateHandle[NavController.KEY_DEEP_LINK_INTENT] = testIntent

            assertIs<CreatePoiScreenState.Form>(SUT.screenState.value)
            assertTrue((SUT.screenState.value as CreatePoiScreenState.Form).suggestion.isSingleImageSuggestion())
            assertEquals(testUri.toString(), (SUT.screenState.value as CreatePoiScreenState.Form).suggestion.imageUrl)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState is Form with WizardSuggestionUiModel data and all fields are null when savedStateHandle contains intent with ACTION_SEND, mimeType is image and no image uri in extras`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            val testIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/any"
            }
            savedStateHandle[NavController.KEY_DEEP_LINK_INTENT] = testIntent

            assertIs<CreatePoiScreenState.Form>(SUT.screenState.value)
            assertNull((SUT.screenState.value as CreatePoiScreenState.Form).suggestion.body)
            assertNull((SUT.screenState.value as CreatePoiScreenState.Form).suggestion.imageUrl)
            assertNull((SUT.screenState.value as CreatePoiScreenState.Form).suggestion.url)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel finishScreen is emiting true when onSave`() =
        runTest {
            whenever(createPoiUseCase(anyNonNull())).thenReturn(Unit)
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.finishScreen.collect() }

            assertNull(SUT.finishScreen.replayCache.firstOrNull())
            SUT.onSave("", "", "", FormImageState("", false), emptyList())
            runCurrent()
            assertTrue(SUT.finishScreen.replayCache.first())
            verify(createPoiUseCase, times(1)).invoke(anyNonNull())
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel initial wizardSuggestionState is None`() =
        runTest {
            assertIs<WizardSuggestionUiState.None>(SUT.wizardSuggestionState.value)
        }

    @Test
    fun `test CreatePoiViewModel wizardSuggestionState is Success when onFetchWizardSuggestion is called with non empty query `() =
        runTest {
            val fakeWizardSuggestion = WizardSuggestion("", "", "", "", emptyList())
            whenever(getWizardSuggestionUseCase(anyNonNull())).thenReturn(fakeWizardSuggestion)

            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.wizardSuggestionState.collect() }

            assertIs<WizardSuggestionUiState.None>(SUT.wizardSuggestionState.value)
            SUT.onFetchWizardSuggestion("any query")
            advanceTimeBy(600)
            runCurrent()
            assertIs<WizardSuggestionUiState.Success>(SUT.wizardSuggestionState.value)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel wizardSuggestionState moves from Success to None when onFetchWizardSuggestion is called with empty query `() =
        runTest {
            val fakeWizardSuggestion = WizardSuggestion("", "", "", "", emptyList())
            whenever(getWizardSuggestionUseCase(anyNonNull())).thenReturn(fakeWizardSuggestion)

            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.wizardSuggestionState.collect() }

            assertIs<WizardSuggestionUiState.None>(SUT.wizardSuggestionState.value)
            SUT.onFetchWizardSuggestion("any query")
            advanceTimeBy(600)
            runCurrent()
            assertIs<WizardSuggestionUiState.Success>(SUT.wizardSuggestionState.value)

            SUT.onFetchWizardSuggestion("")
            assertIs<WizardSuggestionUiState.None>(SUT.wizardSuggestionState.value)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState moves from Wizard to Form when onApplyWizardSuggestion`() =
        runTest {
            val fakeWizardSuggestion = WizardSuggestion("", "", "", "", emptyList())

            whenever(getWizardSuggestionUseCase(anyNonNull())).thenReturn(fakeWizardSuggestion)
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }

            assertIs<CreatePoiScreenState.Wizard>(SUT.screenState.value)
            SUT.onFetchWizardSuggestion("any query")
            advanceTimeBy(600)
            runCurrent()
            SUT.onApplyWizardSuggestion()

            assertIs<CreatePoiScreenState.Form>(SUT.screenState.value)
            collectJob.cancel()
        }

    @Test
    fun `test CreatePoiViewModel screenState moves from Wizard to Form when onSkip`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) { SUT.screenState.collect() }
            assertIs<CreatePoiScreenState.Wizard>(SUT.screenState.value)
            SUT.onSkip()
            assertIs<CreatePoiScreenState.Form>(SUT.screenState.value)
            collectJob.cancel()
        }
}