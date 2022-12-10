package com.grishko188.pointofinterest.features.home.ui.models

import com.grishko188.pointofinterest.R

enum class PoiSortByOption {
    SEVERITY, DATE, TITLE, NONE
}

fun PoiSortByOption.toTitle() = when (this) {
    PoiSortByOption.SEVERITY -> R.string.title_sort_by_severity
    PoiSortByOption.DATE -> R.string.title_sort_by_date
    PoiSortByOption.TITLE -> R.string.title_sort_by_title
    else -> R.string.empty
}