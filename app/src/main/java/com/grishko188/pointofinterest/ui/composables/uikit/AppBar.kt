package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grishko188.pointofinterest.core.utils.stringFromResource
import com.grishko188.pointofinterest.navigation.routeToScreen
import com.grishko188.pointofinterest.ui.theme.DarkMainColor

@Composable
fun AppBar(title: String, navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screenTitle = stringFromResource(res = routeToScreen(currentDestination?.route)?.name) ?: title
    TopAppBar(
        title = {
            Text(text = screenTitle, color = DarkMainColor)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = DarkMainColor
        ),
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkMainColor
                    )
                }
            }
        }
    )
}

