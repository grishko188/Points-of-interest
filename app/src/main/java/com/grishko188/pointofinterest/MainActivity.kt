package com.grishko188.pointofinterest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grishko188.pointofinterest.navigation.Navigation
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.navigation.getMainScreens
import com.grishko188.pointofinterest.navigation.routeToScreen
import com.grishko188.pointofinterest.ui.composables.uikit.AppBar
import com.grishko188.pointofinterest.ui.composables.uikit.BottomBar
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointOfInterestTheme(dynamicColor = false) { RootScreen() }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RootScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = routeToScreen(currentDestination?.route)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentScreen == Screen.Home,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        navController.navigate(Screen.CreatePoi.route)
                    }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        topBar = {
            AppBar(title = stringResource(id = R.string.app_name), currentScreen, navController)
        }, bottomBar = {
            if (currentScreen?.isFullScreen == false) {
                BottomBar(navController = navController, currentDestination, items = getMainScreens())
            }
        }) { paddingValues ->
        Navigation(navHostController = navController, paddingValues = paddingValues)
    }
}

@Preview
@Composable
@ExperimentalAnimationApi
fun RootScreenPreview() {
    PointOfInterestTheme(dynamicColor = false) { RootScreen() }
}