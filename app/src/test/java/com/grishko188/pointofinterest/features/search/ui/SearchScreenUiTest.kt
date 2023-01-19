package com.grishko188.pointofinterest.features.search.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.features.search.vm.SearchScreenUiState
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
class SearchScreenUiTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

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
    fun `test SearchContent is displaying empty state with correct message when emptySearchState is true`() {
        composeTestRule.setContent {
            SearchContent(
                emptySearchState = true,
                searchScreenState = SearchScreenUiState.None,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("empty_search")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Start typing your search request")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test SearchContent is displaying empty state with correct message when emptySearchState is false but searchScreenState is SearchScreenUiState_NothingFound`() {
        composeTestRule.setContent {
            SearchContent(
                emptySearchState = false,
                searchScreenState = SearchScreenUiState.NothingFound,
                onNavigate = onNavigate
            )
        }

        composeTestRule.onNodeWithTag("empty_search")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Nothing found")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test SearchContent is displaying content when searchScreenState is SearchScreenUiState_SearchResult`() {
        whenever(onNavigate(anyNonNull(), anyNonNull())).thenReturn(Unit)

        val resultData = arrayListOf(
            PoiListItem(
                id = "1",
                title = "Title",
                subtitle = "Subtitle",
                source = "source",
                imageUrl = "image_url",
                commentsCount = 2,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(
                    CategoryUiModel(
                        id = "1",
                        color = Color.Magenta,
                        title = "Title",
                        isMutableCategory = false,
                        categoryType = CategoryType.GLOBAL
                    ),
                    CategoryUiModel(
                        id = "2",
                        color = Color.Red,
                        title = "Title 2",
                        isMutableCategory = false,
                        categoryType = CategoryType.GLOBAL
                    )
                )
            ),
            PoiListItem(
                id = "2",
                title = "Title 2",
                subtitle = "Subtitle 2",
                source = "source 2",
                imageUrl = "image_url",
                commentsCount = 4,
                modifiedDate = "XX.XX.XXXX",
                categories = arrayListOf(
                    CategoryUiModel(
                        id = "4",
                        color = Color.Gray,
                        title = "Title 4",
                        isMutableCategory = false,
                        categoryType = CategoryType.GLOBAL
                    ),
                    CategoryUiModel(
                        id = "5",
                        color = Color.Green,
                        title = "Title 5",
                        isMutableCategory = false,
                        categoryType = CategoryType.GLOBAL
                    )
                )
            )
        )

        composeTestRule.setContent {
            SearchContent(
                emptySearchState = false,
                searchScreenState = SearchScreenUiState.SearchResult(resultData),
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
}