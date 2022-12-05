package com.grishko188.pointofinterest.features.categories.ui.models

import androidx.annotation.StringRes
import com.grishko188.pointofinterest.R

enum class CategoryAction(@StringRes val title: Int) {
    DELETE(R.string.delete), EDIT(R.string.edit)
}