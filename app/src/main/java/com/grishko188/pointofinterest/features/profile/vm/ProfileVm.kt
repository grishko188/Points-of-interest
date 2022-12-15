package com.grishko188.pointofinterest.features.profile.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grishko188.pointofinterest.features.profile.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileVm @Inject constructor() : ViewModel() {

    val profileScreenUiState = collectProfileSections()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onSettingsToggled(type: ProfileSectionType) {}

    fun onSignInClicked() {}

    private fun collectProfileSections(): Flow<List<ProfileSectionItem>> = flow {
        emit(createMockProfileItems())
    }

    private fun createMockProfileItems(): List<ProfileSectionItem> = arrayListOf<ProfileSectionItem>().apply {
        ProfileSectionType.values().forEach {
            val title = it.toTitle()
            val subtitle = it.toSubTitle()
            val icon = it.toIcon()
            this += when (it) {
                ProfileSectionType.CATEGORIES,
                ProfileSectionType.ABOUT,
                ProfileSectionType.STATISTICS ->
                    ProfileSectionItem.NavigationItem(icon, title, subtitle, true, it)

                ProfileSectionType.GARBAGE_COLLECTOR,
                ProfileSectionType.SYSTEM_THEME,
                ProfileSectionType.DARK_THEME ->
                    ProfileSectionItem.BooleanSettingsItem(icon, title, subtitle, false, true, it)
                ProfileSectionType.ACCOUNT -> ProfileSectionItem.AccountSectionItem(
                    UserInfo(
                        avatarUrl = "https://yt3.ggpht.com/yti/AJo0G0nl_cf5tYETOT3uT5KwTQKwpat0a9T6k5AnQwa29g=s88-c-k-c0x00ffffff-no-rj-mo",
                        fullName = "Nikita Grishko",
                        email = "grishko188@gmail.com"
                    )
                )
            }
        }
    }
}