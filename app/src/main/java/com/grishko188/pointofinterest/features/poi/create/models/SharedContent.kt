package com.grishko188.pointofinterest.features.poi.create.models

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Patterns

data class SharedContent(
    val content: String?,
    val contentType: ContentType
) {
    companion object {
        val EMPTY = SharedContent(null, ContentType.NONE)
    }
}

enum class ContentType {
    URL, PLAIN_TEXT, LOCAL_IMAGE, NONE, MANUAL
}

fun Intent.fetchSharedContent(): SharedContent {
    if (action != Intent.ACTION_SEND) return SharedContent(null, ContentType.MANUAL)

    return if (isTextMimeType()) {
        val textContent = getStringExtra(Intent.EXTRA_TEXT) ?: ""

        if (Patterns.WEB_URL.matcher(textContent).matches()) {
            SharedContent(textContent, ContentType.URL)
        } else {
            SharedContent(textContent, ContentType.PLAIN_TEXT)
        }

    } else if (isImageMimeType()) {

        val imageContent = getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
        if (imageContent != null) {
            SharedContent(imageContent.toString(), ContentType.LOCAL_IMAGE)
        } else {
            SharedContent.EMPTY
        }
    } else {
        SharedContent.EMPTY
    }

}

private fun Intent.isTextMimeType() = type?.startsWith(MIME_TYPE_TEXT) == true
private fun Intent.isImageMimeType() = type?.startsWith(MIME_TYPE_IMAGE) == true

private const val MIME_TYPE_TEXT = "text/"
private const val MIME_TYPE_IMAGE = "image/"