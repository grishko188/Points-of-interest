package com.grishko188.pointofinterest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.grishko188.pointofinterest.navigation.Navigation
import com.grishko188.pointofinterest.navigation.getMainScreens
import com.grishko188.pointofinterest.ui.composables.uikit.AppBar
import com.grishko188.pointofinterest.ui.composables.uikit.BottomBar
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointOfInterestTheme(dynamicColor = false) { RootScreen() }
        }
    }
}

@Composable
fun RootScreen() {
    // A surface container using the 'background' color from the theme
    val navController = rememberNavController()
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        AppBar(title = stringResource(id = R.string.app_name), navController)
    }, bottomBar = {
        BottomBar(navController = navController, items = getMainScreens())
    }) { paddingValues ->
        Navigation(navHostController = navController, paddingValues = paddingValues)
    }
}