package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grishko188.pointofinterest.core.utils.stringFromResource
import com.grishko188.pointofinterest.navigation.getMainScreens
import com.grishko188.pointofinterest.navigation.routeToScreen
import com.grishko188.pointofinterest.ui.theme.DarkMainColor

@Composable
fun AppBar(title: String, navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = routeToScreen(currentDestination?.route)
    val screenTitle = stringFromResource(res = currentScreen?.name) ?: title
    TopAppBar(
        title = {
            Text(text = screenTitle, color = MaterialTheme.colorScheme.onBackground)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            if (navController.previousBackStackEntry != null && currentScreen !in getMainScreens()) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}

