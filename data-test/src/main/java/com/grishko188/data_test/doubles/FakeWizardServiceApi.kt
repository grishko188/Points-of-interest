package com.grishko188.data_test.doubles

import android.content.Context
import com.grishko188.data.features.poi.api.WizardServiceApi
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FakeWizardServiceApi @Inject constructor(
    @ApplicationContext private val context: Context
) : WizardServiceApi {

    override suspend fun getUrlContent(contentUrl: String): ResponseBody {
        val testHtmlPage = getMockData("test/test.html")
        val mockMediaType = "text/html".toMediaType()
        return testHtmlPage.toResponseBody(mockMediaType)
    }

    private suspend fun getMockData(fileName: String) = suspendCoroutine { continuation ->
        context.assets.open(fileName).bufferedReader().use {
            it.readText().let { text -> continuation.resume(text) }
        }
    }
}