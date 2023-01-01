package com.grishko188.pointofinterest.features.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.navigation.Navigation
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.navigation.getMainScreens
import com.grishko188.pointofinterest.ui.composables.uikit.AppBar
import com.grishko188.pointofinterest.ui.composables.uikit.BottomBar
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme

@ExperimentalAnimationApi
@Composable
fun PoiMainScreen(
    appState: PoiAppState = rememberPoiAppState()
) {

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        snackbarHost = {
            SnackbarHost(hostState = appState.snackBarHostState) {
                Snackbar(snackbarData = it, actionColor = MaterialTheme.colorScheme.secondary)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = appState.isCurrentFullScreen.not(),
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        appState.navigateTo(Screen.CreatePoi)
                    }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        topBar = { AppBar(title = stringResource(id = R.string.app_name), appState) },
        bottomBar = {
            if (appState.isCurrentFullScreen.not()) {
                BottomBar(appState, items = getMainScreens())
            }
        }) { paddingValues -> Navigation(appState = appState, paddingValues = paddingValues) }
}

@Preview
@Composable
@ExperimentalAnimationApi
fun RootScreenPreview() {
    PointOfInterestTheme() { PoiMainScreen() }
}