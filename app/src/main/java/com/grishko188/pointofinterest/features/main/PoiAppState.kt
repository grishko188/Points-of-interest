package com.grishko188.pointofinterest.features.main

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grishko188.pointofinterest.navigation.MenuActionType
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.navigation.getMainScreens
import com.grishko188.pointofinterest.navigation.routeToScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberPoiAppState(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    searchState: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
): PoiAppState {
    return remember(navController, snackBarHostState, coroutineScope, searchState) {
        PoiAppState(navController, snackBarHostState, coroutineScope, searchState)
    }
}

@Stable
class PoiAppState(
    val navController: NavHostController,
    val snackBarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope,
    val searchState: MutableState<TextFieldValue>
) {
    val navBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val currentDestination: NavDestination?
        @Composable get() = navBackStackEntry?.destination

    val currentScreen: Screen?
        @Composable get() = routeToScreen(currentDestination?.route)

    val isRootScreen: Boolean
        @Composable get() = currentScreen in getMainScreens() || currentScreen == null

    val isCurrentFullScreen: Boolean
        @Composable get() = currentScreen?.isFullScreen == true

    var showSearchBarState by mutableStateOf(false)

    var showSortDialog by mutableStateOf(false)

    private val menuItemClickObservers = mutableMapOf<MenuActionType, OnMenuItemListener>()

    fun registerMenuItemClickObserver(actionType: MenuActionType, menuItemListener: OnMenuItemListener) {
        menuItemClickObservers[actionType] = menuItemListener
    }

    fun disposeMenuItemObserver(actionType: MenuActionType) {
        menuItemClickObservers.remove(actionType)
    }

    fun navigateToRoot(screen: Screen) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateTo(screen: Screen, arguments: List<Pair<String, Any>> = emptyList()) {
        val route = if (arguments.isEmpty().not()) {
            screen.routePath + "?" + arguments.joinToString(separator = ",") { "${it.first}=${it.second}" }
        } else {
            screen.route
        }
        navController.navigate(route)
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    fun onMenuItemClicked(actionType: MenuActionType) {
        when (actionType) {
            MenuActionType.BACK -> onBackClick()
            MenuActionType.SEARCH -> showSearchBarState = true
            MenuActionType.ADD -> navigateTo(Screen.CategoriesDetailed)
            MenuActionType.SORT -> showSortDialog = true
            else -> menuItemClickObservers[actionType]?.onMenuItemClicked(actionType)
        }
    }
}

interface OnMenuItemListener {
    fun onMenuItemClicked(menuActionType: MenuActionType)
}