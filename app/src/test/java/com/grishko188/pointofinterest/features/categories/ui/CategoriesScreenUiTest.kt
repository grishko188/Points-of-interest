package com.grishko188.pointofinterest.features.categories.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.navigation.Screen
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
class CategoriesScreenUiTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var onNavigate: (Screen, List<Pair<String, Any>>) -> Unit

    @Mock
    lateinit var onDeleteItem: (String) -> Unit

    @Mock
    lateinit var onCommitDeleteItem: (String) -> Unit

    @Mock
    lateinit var onUndoDeleteItem: (String) -> Unit

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

    @Test
    fun `test CategoriesContent displays all categories where mutable categories are clickable and others not`() {
        whenever(onNavigate(anyNonNull(), anyNonNull())).thenReturn(Unit)

        val testCategories = hashMapOf<Int, List<CategoryUiModel>>().apply {
            put(
                android.R.string.search_go,
                listOf(
                    CategoryUiModel("1", Color.Green, "SEVERITY", false, CategoryType.SEVERITY),
                    CategoryUiModel("2", Color.Yellow, "SEVERITY 2", false, CategoryType.SEVERITY),
                    CategoryUiModel("3", Color.Magenta, "SEVERITY 3", false, CategoryType.SEVERITY)
                )
            )

            put(
                android.R.string.VideoView_error_button,
                listOf(
                    CategoryUiModel("4", Color.Green, "PERSONAL", true, CategoryType.PERSONAL),
                    CategoryUiModel("5", Color.Yellow, "PERSONAL 2", true, CategoryType.PERSONAL),
                    CategoryUiModel("6", Color.Magenta, "PERSONAL 3", true, CategoryType.PERSONAL)
                )
            )
        }

        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            CategoriesContent(
                onNavigate = onNavigate,
                onDeleteItem = onDeleteItem,
                onCommitDeleteItem = onCommitDeleteItem,
                onUndoDeleteItem = onUndoDeleteItem,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                categories = testCategories,
                itemsToDelete = emptyList()
            )
        }

        composeTestRule.onNodeWithTag("categories_screen_full_list")
            .assertExists()
            .assertIsDisplayed()

        testCategories.values.flatten().forEach { model ->
            composeTestRule.onNodeWithText(model.title)
                .assertExists()
                .assertIsDisplayed()
                .also {
                    if (model.isMutableCategory) it.assertHasClickAction().performClick()
                    else it.assertHasNoClickAction()
                }
        }

        verify(onNavigate, times(testCategories.values.flatten().filter { it.isMutableCategory }.size)).invoke(anyNonNull(), anyNonNull())
    }

    @Test
    fun `test CategoriesContent displays all categories except itemsToDelete`() {
        val testCategories = hashMapOf<Int, List<CategoryUiModel>>().apply {
            put(
                android.R.string.search_go,
                listOf(
                    CategoryUiModel("1", Color.Green, "SEVERITY", false, CategoryType.SEVERITY),
                    CategoryUiModel("2", Color.Yellow, "SEVERITY 2", false, CategoryType.SEVERITY),
                    CategoryUiModel("3", Color.Magenta, "SEVERITY 3", false, CategoryType.SEVERITY)
                )
            )

            put(
                android.R.string.VideoView_error_button,
                listOf(
                    CategoryUiModel("4", Color.Green, "PERSONAL", true, CategoryType.PERSONAL),
                    CategoryUiModel("5", Color.Yellow, "PERSONAL 2", true, CategoryType.PERSONAL),
                    CategoryUiModel("6", Color.Magenta, "PERSONAL 3", true, CategoryType.PERSONAL)
                )
            )
        }

        val itemsToDelete = arrayListOf("4")

        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            CategoriesContent(
                onNavigate = onNavigate,
                onDeleteItem = onDeleteItem,
                onCommitDeleteItem = onCommitDeleteItem,
                onUndoDeleteItem = onUndoDeleteItem,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                categories = testCategories,
                itemsToDelete = itemsToDelete
            )
        }

        composeTestRule.onNodeWithTag("categories_screen_full_list")
            .assertExists()
            .assertIsDisplayed()

        testCategories.values.flatten().forEach { model ->
            if (model.id in itemsToDelete) {
                composeTestRule.onNodeWithText(model.title)
                    .assertDoesNotExist()
            } else {
                composeTestRule.onNodeWithText(model.title)
                    .assertExists()
                    .assertIsDisplayed()
            }
        }
    }
}