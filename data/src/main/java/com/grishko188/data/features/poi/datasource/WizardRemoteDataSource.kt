package com.grishko188.data.features.poi.datasource

import android.webkit.URLUtil
import com.grishko188.data.features.poi.api.WizardServiceApi
import com.grishko188.data.features.poi.model.WizardSuggestionDataModel
import okhttp3.MediaType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI
import javax.inject.Inject

class WizardRemoteDataSource @Inject constructor(
    private val api: WizardServiceApi
) : WizardDataSource {

    override suspend fun getWizardSuggestion(url: String): WizardSuggestionDataModel {
        val responseBody = api.getUrlContent(url)
        val contentType = responseBody.contentType()
        return if (contentType.isImageContentType()) {
            WizardSuggestionDataModel(contentUrl = url, imageUrl = url)
        } else {
            val document = Jsoup.parse(responseBody.string())
            val suggestion = document.toSuggestion(url)
            suggestion
        }
    }

    private fun MediaType?.isImageContentType() = this?.type == CONTENT_TYPE_IMAGE || this?.type == CONTENT_TYPE_BINARY

    private fun Document.toSuggestion(originalUrl: String): WizardSuggestionDataModel {
        var title: String? = select("meta[property=og:title]").attr("content")
        if (title.isNullOrEmpty()) {
            title = title()
        }

        var body: String? = select("meta[property=og:description]").attr("content")
        if (body.isNullOrEmpty()) {
            body = select("meta[name=Description]").attr("content")
        }
        if (body.isNullOrEmpty()) {
            body = select("meta[name=description]").attr("content")
        }

        var image: String? = select("meta[property=og:image]").attr("content").resolveUrl(originalUrl)
        if (image.isNullOrEmpty()) {
            image = select("link[rel=image_src]").attr("href").resolveUrl(originalUrl)
        }
        if (image.isNullOrEmpty()) {
            image = select("link[rel=apple-touch-icon]").attr("href").resolveUrl(originalUrl)
        }
        if (image.isNullOrEmpty()) {
            image = select("link[rel=icon]").attr("href").resolveUrl(originalUrl)
        }

        return WizardSuggestionDataModel(
            contentUrl = originalUrl,
            title = title,
            body = body,
            imageUrl = image
        )
    }

    private fun String?.resolveUrl(url: String): String? {
        return when {
            this.isNullOrEmpty() -> null
            URLUtil.isValidUrl(this) -> this
            else -> kotlin.runCatching { URI(url).resolve(this).toString() }.getOrNull()
        }
    }

    companion object {
        private const val CONTENT_TYPE_IMAGE = "image"
        private const val CONTENT_TYPE_BINARY = "binary"
    }
}