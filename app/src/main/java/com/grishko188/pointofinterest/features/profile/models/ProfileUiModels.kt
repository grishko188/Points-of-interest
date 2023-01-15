package com.grishko188.pointofinterest.features.profile.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.grishko188.pointofinterest.R

sealed class ProfileScreenUiState {
    object Empty : ProfileScreenUiState()
    data class Result(val sections: List<ProfileSectionItem>) : ProfileScreenUiState()
}

sealed class ProfileSectionItem(val type: ProfileSectionType) {
    data class AccountSectionItem(val userInfo: UserInfo?) : ProfileSectionItem(ProfileSectionType.ACCOUNT)
    data class NavigationItem(
        @DrawableRes val icon: Int,
        @StringRes val title: Int,
        @StringRes val subtitle: Int? = null,
        val isEnabled: Boolean,
        val sectionType: ProfileSectionType
    ) : ProfileSectionItem(sectionType)

    data class BooleanSettingsItem(
        @DrawableRes val icon: Int,
        @StringRes val title: Int,
        @StringRes val subtitle: Int? = null,
        val state: Boolean,
        val isEnabled: Boolean,
        val sectionType: ProfileSectionType
    ) : ProfileSectionItem(sectionType)
}

enum class ProfileSectionType {
    ACCOUNT, STATISTICS, CATEGORIES, SYSTEM_THEME, DARK_THEME, GARBAGE_COLLECTOR, ABOUT
}

val wipSections = arrayListOf(ProfileSectionType.STATISTICS)

data class UserInfo(
    val fullName: String,
    val email: String,
    val avatarUrl: String?
)

fun ProfileSectionType.toTitle(): Int =
    when (this) {
        ProfileSectionType.ACCOUNT -> -1
        ProfileSectionType.STATISTICS -> R.string.profile_section_title_stats
        ProfileSectionType.CATEGORIES -> R.string.profile_section_title_categories
        ProfileSectionType.SYSTEM_THEME -> R.string.profile_section_title_system_theme
        ProfileSectionType.DARK_THEME -> R.string.profile_section_title_dark_theme
        ProfileSectionType.GARBAGE_COLLECTOR -> R.string.profile_section_title_gc
        ProfileSectionType.ABOUT -> R.string.profile_section_title_about_app
    }

fun ProfileSectionType.toSubTitle(): Int? =
    when (this) {
        ProfileSectionType.SYSTEM_THEME -> R.string.profile_section_subtitle_system_theme
        ProfileSectionType.GARBAGE_COLLECTOR -> R.string.profile_section_subtitle_gc
        else -> null
    }

fun ProfileSectionType.toIcon(): Int =
    when (this) {
        ProfileSectionType.ACCOUNT -> -1
        ProfileSectionType.STATISTICS -> R.drawable.ic_stats
        ProfileSectionType.CATEGORIES -> R.drawable.ic_categories_filled
        ProfileSectionType.SYSTEM_THEME -> R.drawable.ic_system_settings
        ProfileSectionType.DARK_THEME -> R.drawable.ic_dark_mode
        ProfileSectionType.GARBAGE_COLLECTOR -> R.drawable.ic_auto_gc
        ProfileSectionType.ABOUT -> R.drawable.ic_info
    }