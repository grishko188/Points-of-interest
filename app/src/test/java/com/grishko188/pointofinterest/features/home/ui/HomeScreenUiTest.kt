package com.grishko188.pointofinterest.features.home.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.anyNotNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.core.utils.ErrorDisplayObject
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.features.home.ui.models.PoiSortByUiOption
import com.grishko188.pointofinterest.features.home.vm.HomeViewModel
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
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class HomeScreenUiTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var onRetry: () -> Unit

    @Mock
    lateinit var onApplySortByOption: (PoiSortByUiOption) -> Unit

    @Mock
    lateinit var onCloseSortDialog: () -> Unit

    @Mock
    lateinit var onNavigate: (Screen, List<Pair<String, Any>>) -> Unit

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
    fun `test HomeScreenContent displays loading when HomeUiContentState_Loading`() {
        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Loading,
                categoriesState = emptyList(),
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("progress_view")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate))
    }

    @Test
    fun `test HomeScreenContent displays empty  when HomeUiContentState_Empty`() {
        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Empty,
                categoriesState = emptyList(),
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("empty_view")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test HomeScreenContent displays error with retry button when HomeUiContentState_Error`() {
        whenever(onRetry()).thenReturn(Unit)

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Error(ErrorDisplayObject.GenericError),
                categoriesState = emptyList(),
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("error_view")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("try again", ignoreCase = true)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(onRetry, times(1)).invoke()
    }

    @Test
    fun `test HomeScreenContent HomeScreenFilterContent is exist and behave as expected when categoriesState is list of categories`() {

        val testCategories = arrayListOf(
            CategoryUiModel("1", Color.Green, "Title", true, CategoryType.PERSONAL),
            CategoryUiModel("2", Color.Yellow, "Title 2", true, CategoryType.PERSONAL),
            CategoryUiModel("3", Color.Magenta, "Title 3", true, CategoryType.PERSONAL)
        )

        val selectedFilters = mutableListOf<String>()

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Loading,
                categoriesState = testCategories,
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = selectedFilters,
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("filters_horizontal_collection")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(testCategories.size + 1) // ADD MORE button
            .onFirst()
            .assertHasClickAction()
            .performClick()

        assertEquals(testCategories.first().id, selectedFilters.first())
    }

    @Test
    fun `test HomeScreenContent HomeScreenFilterContent contains add more button which invokes onNavigate function`() {

        whenever(onNavigate(anyNonNull(), anyNonNull())).thenReturn(Unit)

        val testCategories = arrayListOf(
            CategoryUiModel("1", Color.Green, "Title", true, CategoryType.PERSONAL),
            CategoryUiModel("2", Color.Yellow, "Title 2", true, CategoryType.PERSONAL),
            CategoryUiModel("3", Color.Magenta, "Title 3", true, CategoryType.PERSONAL)
        )

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Loading,
                categoriesState = testCategories,
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }
        composeTestRule.onRoot().printToLog(HomeScreenUiTest::class.java.simpleName)

        composeTestRule.onNodeWithText("Add more")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(onNavigate, times(1)).invoke(anyNonNull(), anyNonNull())
    }

    @Test
    fun `test HomeScreenContent PoiListContent is exist when homeContentState is HomeUiContentState_Result and item click invoke onNavigate`() {
        whenever(onNavigate(anyNotNull(), anyNonNull())).thenReturn(Unit)

        val testCategories = arrayListOf(
            CategoryUiModel("1", Color.Green, "Title", true, CategoryType.PERSONAL),
            CategoryUiModel("2", Color.Yellow, "Title 2", true, CategoryType.PERSONAL),
            CategoryUiModel("3", Color.Magenta, "Title 3", true, CategoryType.PERSONAL)
        )

        val resultData = arrayListOf(
            PoiListItem(
                id = "1",
                title = "Title",
                subtitle = "Subtitle",
                source = "source",
                imageUrl = "image_url",
                commentsCount = 2,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(testCategories[0], testCategories[1])
            ),
            PoiListItem(
                id = "2",
                title = "Title 2",
                subtitle = "Subtitle 2",
                source = "source 2",
                imageUrl = "image_url",
                commentsCount = 4,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(testCategories[0], testCategories[2])
            )
        )

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Result(resultData),
                categoriesState = testCategories,
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("poi_content_list")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(resultData.size)
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(onNavigate, times(1)).invoke(anyNonNull(), anyNonNull())
    }

    @Test
    fun `test HomeScreenContent PoiListContent is HomeUiContentState_Empty when selectedFiltersState has not matching filter ids`() {
        val testCategories = arrayListOf(
            CategoryUiModel("1", Color.Green, "Title", true, CategoryType.PERSONAL),
            CategoryUiModel("2", Color.Yellow, "Title 2", true, CategoryType.PERSONAL),
            CategoryUiModel("3", Color.Magenta, "Title 3", true, CategoryType.PERSONAL)
        )

        val resultData = arrayListOf(
            PoiListItem(
                id = "1",
                title = "Title",
                subtitle = "Subtitle",
                source = "source",
                imageUrl = "image_url",
                commentsCount = 2,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(testCategories[0], testCategories[1])
            ),
            PoiListItem(
                id = "2",
                title = "Title 2",
                subtitle = "Subtitle 2",
                source = "source 2",
                imageUrl = "image_url",
                commentsCount = 4,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(testCategories[0], testCategories[2])
            )
        )

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Result(resultData),
                categoriesState = testCategories,
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf("1", "2", "3"),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("poi_content_list")
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("empty_view")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test HomeScreenContent displays dialog when showSortDialogState is true`() {
        whenever(onApplySortByOption(anyNonNull())).thenReturn(Unit)
        whenever(onCloseSortDialog()).thenReturn(Unit)

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Loading,
                categoriesState = emptyList(),
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = true,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsDialog))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("sort_selector${PoiSortByUiOption.DATE.name}")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithText("cancel", ignoreCase = true)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(onApplySortByOption, times(1)).invoke(anyNonNull())
        verify(onCloseSortDialog, times(2)).invoke()
    }

    @Test
    fun `test HomeScreenContent sorting dialog is not displayed when showSortDialogState is false`() {

        composeTestRule.setContent {
            HomeScreenContent(
                homeContentState = HomeViewModel.HomeUiContentState.Loading,
                categoriesState = emptyList(),
                selectedSortByOption = PoiSortByUiOption.DATE,
                selectedFiltersState = mutableListOf(),
                showSortDialogState = false,
                onRetry = onRetry,
                onApplySortByOption = onApplySortByOption,
                onCloseSortDialog = onCloseSortDialog,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsDialog))
            .assertDoesNotExist()
    }
}