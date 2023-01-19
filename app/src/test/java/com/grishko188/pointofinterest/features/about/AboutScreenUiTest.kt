package com.grishko188.pointofinterest.features.about

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class AboutScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `test AboutScreen content is displayed`() {
        composeTestRule.setContent {
            AboutScreen()
        }

        composeTestRule.onRoot().printToLog(AboutScreenUiTest::class.java.simpleName)

        composeTestRule
            .onNodeWithContentDescription("Application logo")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("POINT OF INTEREST", ignoreCase = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("App description")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Developed by")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("Git hub profile link")
            .assertIsDisplayed()
    }
}