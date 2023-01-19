package com.grishko188.pointofinterest.features.profile.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.anyNotNull
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.profile.models.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class ProfileScreenUiTest {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var onSignInClicked: () -> Unit

    @Mock
    lateinit var onSignOutClicked: () -> Unit

    @Mock
    lateinit var onNavigateInternal: (ProfileSectionType) -> Unit

    @Mock
    lateinit var onSettingsToggled: (ProfileSectionType, Boolean) -> Unit

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        ShadowLog.stream = System.out
    }

    @After
    fun teardown() {
        validateMockitoUsage()
    }

    @Test
    fun `test ProfileScreenContent root lazy column is displayed and contains all expected children`() {
        composeTestRule.setContent {
            ProfileScreenContent(
                profileSections = generateProfileScreenTestSections(false),
                onSignInClicked = onSignInClicked,
                onSignOutClicked = onSignOutClicked,
                onNavigateInternal = onNavigateInternal,
                onSettingsToggled = onSettingsToggled
            )
        }

        composeTestRule.onRoot().printToLog(ProfileScreenUiTest::class.java.simpleName)

        composeTestRule.onNodeWithTag("profile_section_content_lazy_list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(ProfileSectionType.values().filter { it !in wipSections }.size)
    }


    @Test
    fun `test ProfileScreenContent AccountSection content and behaviour when user is not authorized`() {
        whenever(onSignInClicked()).thenReturn(Unit)

        composeTestRule.setContent {
            ProfileScreenContent(
                profileSections = generateProfileScreenTestSections(false),
                onSignInClicked = onSignInClicked,
                onSignOutClicked = onSignOutClicked,
                onNavigateInternal = onNavigateInternal,
                onSettingsToggled = onSettingsToggled
            )
        }

        composeTestRule.onRoot().printToLog(ProfileScreenUiTest::class.java.simpleName)

        composeTestRule.onNodeWithTag("account_section").assertIsDisplayed()

        composeTestRule.onNodeWithTag("account_user_information").assertDoesNotExist()

        composeTestRule.onNodeWithTag("account_sign_in_container")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(onSignInClicked, times(1)).invoke()
    }

    @Test
    fun `test ProfileScreenContent AccountSection content and behaviour when user is authorized`() {
        whenever(onSignOutClicked()).thenReturn(Unit)

        composeTestRule.setContent {
            ProfileScreenContent(
                profileSections = generateProfileScreenTestSections(true),
                onSignInClicked = onSignInClicked,
                onSignOutClicked = onSignOutClicked,
                onNavigateInternal = onNavigateInternal,
                onSettingsToggled = onSettingsToggled
            )
        }

        composeTestRule.onRoot().printToLog(ProfileScreenUiTest::class.java.simpleName)

        composeTestRule.onNodeWithTag("account_section").assertIsDisplayed()

        composeTestRule.onNodeWithTag("account_user_information")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("account_sign_in_container")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Name")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("email@gmail.com")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("User image placeholder")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("account_sign_out_button")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(onSignOutClicked, times(1)).invoke()
    }

    @Test
    fun `test ProfileScreenContent NavigationSection content and behaviour`() {
        whenever(onNavigateInternal(anyNonNull())).thenReturn(Unit)

        val testSections = generateProfileScreenTestSections(true)
        composeTestRule.setContent {
            ProfileScreenContent(
                profileSections = testSections,
                onSignInClicked = onSignInClicked,
                onSignOutClicked = onSignOutClicked,
                onNavigateInternal = onNavigateInternal,
                onSettingsToggled = onSettingsToggled
            )
        }

        composeTestRule.onRoot().printToLog(ProfileScreenUiTest::class.java.simpleName)
        val navigationSections = testSections.filterIsInstance<ProfileSectionItem.NavigationItem>()
        navigationSections.forEach { navigationItem ->
            composeTestRule.onNodeWithTag("navigation_section:${navigationItem.sectionType}")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }

        verify(onNavigateInternal, times(navigationSections.size)).invoke(anyNotNull())
    }

    @Test
    fun `test ProfileScreenContent BooleanSettingsSection content and behaviour`() {
        whenever(onSettingsToggled(anyNonNull(), anyBoolean())).thenReturn(Unit)

        val testSections = generateProfileScreenTestSections(true)
        composeTestRule.setContent {
            ProfileScreenContent(
                profileSections = testSections,
                onSignInClicked = onSignInClicked,
                onSignOutClicked = onSignOutClicked,
                onNavigateInternal = onNavigateInternal,
                onSettingsToggled = onSettingsToggled
            )
        }

        composeTestRule.onRoot().printToLog(ProfileScreenUiTest::class.java.simpleName)
        val booleanSections = testSections.filterIsInstance<ProfileSectionItem.BooleanSettingsItem>()
        booleanSections.forEach { booleanItem ->
            composeTestRule.onNodeWithTag("boolean_settings_section:${booleanItem.sectionType}")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }

        verify(onSettingsToggled, times(booleanSections.size)).invoke(anyNotNull(), anyBoolean())
    }


    private fun generateProfileScreenTestSections(isAuthorized: Boolean) = ProfileSectionType.values().filter { it !in wipSections }.map {
        val title = it.toTitle()
        val subtitle = it.toSubTitle()
        val icon = it.toIcon()

        when (it) {
            ProfileSectionType.CATEGORIES,
            ProfileSectionType.ABOUT,
            ProfileSectionType.STATISTICS ->
                ProfileSectionItem.NavigationItem(icon, title, subtitle, true, it)

            ProfileSectionType.GARBAGE_COLLECTOR ->
                ProfileSectionItem.BooleanSettingsItem(icon, title, subtitle, state = false, isEnabled = true, sectionType = it)
            ProfileSectionType.SYSTEM_THEME ->
                ProfileSectionItem.BooleanSettingsItem(icon, title, subtitle, state = true, isEnabled = true, sectionType = it)
            ProfileSectionType.DARK_THEME ->
                ProfileSectionItem.BooleanSettingsItem(icon, title, subtitle, state = false, isEnabled = true, sectionType = it)

            ProfileSectionType.ACCOUNT -> ProfileSectionItem.AccountSectionItem(
                userInfo = if (isAuthorized)
                    UserInfo(
                        avatarUrl = null,
                        fullName = "Name",
                        email = "email@gmail.com"
                    )
                else
                    null
            )
        }

    }
}