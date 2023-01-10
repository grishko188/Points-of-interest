package com.grishko188.data.poi.datasource

import android.content.Context
import com.grishko188.data.MockitoHelper.mock
import com.grishko188.data.MockitoHelper.whenever
import com.grishko188.data.core.Remote
import com.grishko188.data.di.ApiModule
import com.grishko188.data.features.poi.api.WizardServiceApi
import com.grishko188.data.features.poi.datasource.WizardDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class WizardSuggestionDataSourceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @Mock
    lateinit var mockApi: WizardServiceApi

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    @Remote
    lateinit var SUT: WizardDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_getWizardSuggestion_for_image_url_returns_suggestion_with_imageUrl_only() = runTest {
        val mockResponseBody = mock<ResponseBody>()
        val mockMediaType = "image/png".toMediaType()
        val mockUrl = "https://www.some.com/imageurl"
        whenever(mockApi.getUrlContent(mockUrl)).thenReturn(mockResponseBody)
        whenever(mockResponseBody.contentType()).thenReturn(mockMediaType)

        val suggestion = SUT.getWizardSuggestion(mockUrl)

        assertNotNull(suggestion)
        assertEquals(mockUrl, suggestion.imageUrl)
        assertNull(suggestion.body)
        assertNull(suggestion.title)
        assertNull(suggestion.tags)
    }

    @Test
    fun test_getWizardSuggestion_for_html_url_returns_suggestion_with_values() = runTest {
        val testHtmlPage = getMockData("test/test.html")
        val mockMediaType = "text/html".toMediaType()
        val mockResponseBody = testHtmlPage.toResponseBody(mockMediaType)

        val mockUrl = "https://developer.android.com"

        whenever(mockApi.getUrlContent(mockUrl)).thenReturn(mockResponseBody)

        val suggestion = SUT.getWizardSuggestion(mockUrl)

        assertNotNull(suggestion)
        assertEquals(mockUrl, suggestion.contentUrl)
        assertNotNull(suggestion.title)
        assertNotNull(suggestion.imageUrl)
    }

    @Test
    fun test_getWizardSuggestion_for_non_html_url_returns_empty_suggestions() = runTest {
        val testJsonFile = getMockData("categories/poi_categories_default.json")
        val mockMediaType = "application/json".toMediaType()
        val mockResponseBody = testJsonFile.toResponseBody(mockMediaType)

        val mockUrl = "https://developer.android.com"

        whenever(mockApi.getUrlContent(mockUrl)).thenReturn(mockResponseBody)

        val suggestion = SUT.getWizardSuggestion(mockUrl)

        assertNotNull(suggestion)
        assertEquals(mockUrl, suggestion.contentUrl)
        assertNull(suggestion.title)
        assertNull(suggestion.imageUrl)
        assertNull(suggestion.body)
    }

    private suspend fun getMockData(fileName: String) = suspendCoroutine { continuation ->
        context.assets.open(fileName).bufferedReader().use {
            it.readText().let { text -> continuation.resume(text) }
        }
    }
}