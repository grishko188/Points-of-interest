package com.grishko188.data.features.categories.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import com.grishko188.data.core.UNSPECIFIED_ID

data class CategoryRemote(
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String,
    @SerializedName("type") val type: String
)

fun CategoryRemote.toDataModel() = CategoryDataModel(
    id = UNSPECIFIED_ID,
    title = title,
    color = color.toColorInt(),
    type = type,
    isMutable = false
)

private fun String.toColorInt() = Color.parseColor(this)