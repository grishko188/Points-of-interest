package com.grishko188.pointofinterest.features.categories.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.TextFieldValue
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.ui.models.DetailedCategoriesUiState
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
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class CategoriesDetailedScreenUiTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var onTextChanged: (TextFieldValue) -> Unit

    @Mock
    lateinit var onColorSelected: (Color) -> Unit

    @Mock
    lateinit var onSave: (String, Color) -> Unit

    @Mock
    lateinit var onUpdate: (CategoryUiModel, String, Color) -> Unit

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        ShadowLog.stream = System.out
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `test CategoriesDetailedContent displays progress when uiState is DetailedCategoriesUiState_Loading`() {
        composeTestRule.setContent {
            val focusRequester = remember { FocusRequester() }

            CategoriesDetailedContent(
                uiState = DetailedCategoriesUiState.Loading,
                selectedColor = Color.Red,
                focusRequester = focusRequester,
                keyboardController = LocalSoftwareKeyboardController.current,
                textFieldValue = TextFieldValue(""),
                onTextChanged = onTextChanged,
                onColorSelected = onColorSelected,
                onSave = onSave,
                onUpdate = onUpdate
            )
        }

        composeTestRule.onNodeWithTag("progress_view")
            .assertExists()
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `test CategoriesDetailedContent displays CategoriesDetailedSuccessContent when uiState is DetailedCategoriesUiState_Success`() {
        composeTestRule.setContent {
            val focusRequester = remember { FocusRequester() }

            CategoriesDetailedContent(
                uiState = DetailedCategoriesUiState.Success(null),
                selectedColor = Color.Red,
                focusRequester = focusRequester,
                keyboardController = LocalSoftwareKeyboardController.current,
                textFieldValue = TextFieldValue(""),
                onTextChanged = onTextChanged,
                onColorSelected = onColorSelected,
                onSave = onSave,
                onUpdate = onUpdate
            )
        }

        composeTestRule.onNodeWithTag("progress_view")
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("detailed_category_content")
            .assertExists()
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `test CategoriesDetailedSuccessContent empty screen behaviour when uiState is DetailedCategoriesUiState_Success and selectedCategory is null`() {
        whenever(onTextChanged(anyNonNull())).thenReturn(Unit)
        whenever(onColorSelected(anyNonNull())).thenReturn(Unit)

        composeTestRule.setContent {
            val focusRequester = remember { FocusRequester() }

            CategoriesDetailedContent(
                uiState = DetailedCategoriesUiState.Success(null),
                selectedColor = Color.Transparent,
                focusRequester = focusRequester,
                keyboardController = LocalSoftwareKeyboardController.current,
                textFieldValue = TextFieldValue(""),
                onTextChanged = onTextChanged,
                onColorSelected = onColorSelected,
                onSave = onSave,
                onUpdate = onUpdate
            )
        }

        composeTestRule.onNodeWithContentDescription("Clear text icon")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Save", ignoreCase = true)
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()

        composeTestRule.onNodeWithTag("grid_color_pallete")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .filter(SemanticsMatcher.keyIsDefined(SemanticsProperties.HorizontalScrollAxisRange))
            .onFirst()
            .onChildren()
            .filter(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assertAll(SemanticsMatcher.expectValue(SemanticsProperties.Selected, false))
            .onFirst()
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithTag("original_selected_color_item")
            .assertDoesNotExist()

        composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.EditableText))
            .assertExists()
            .assertIsDisplayed()
            .performTextInput("Test")

        verify(onColorSelected, times(1)).invoke(anyNonNull())
        verify(onTextChanged, times(1)).invoke(anyNonNull())
    }
}