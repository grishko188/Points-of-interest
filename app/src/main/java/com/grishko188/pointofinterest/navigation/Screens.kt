package com.grishko188.pointofinterest.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.grishko188.pointofinterest.R

sealed class Screen(
    val route: String,
    @StringRes val name: Int,
    @DrawableRes val icon: Int? = null,
    val isFullScreen: Boolean = false,
    val menuItems: List<MenuItem> = emptyList()
) {
    object Home : Screen(
        route = "screen_home",
        R.string.screen_home,
        R.drawable.ic_home,
        menuItems = arrayListOf(MenuItem.Search)
    )

    object Profile : Screen(route = "screen_profile", R.string.screen_profile, R.drawable.ic_profile)

    object Categories : Screen(route = "screen_categories", R.string.screen_categories, R.drawable.ic_category, isFullScreen = true)
    object CreatePoi : Screen(route = "screen_create_poi", R.string.screen_create_poi, isFullScreen = true)

    companion object {
        val all = arrayListOf(Home, Categories, Profile, CreatePoi)
    }
}

fun getMainScreens() = arrayListOf(Screen.Home, Screen.Profile)

fun routeToScreen(route: String?): Screen? = Screen.all.find { it.route == route }