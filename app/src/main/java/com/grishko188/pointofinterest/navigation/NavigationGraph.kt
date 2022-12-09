package com.grishko188.pointofinterest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grishko188.pointofinterest.features.categories.categoriesGraph
import com.grishko188.pointofinterest.features.categories.ui.CategoriesScreen
import com.grishko188.pointofinterest.features.profile.ui.ProfileScreen
import com.grishko188.pointofinterest.features.home.ui.HomeScreen
import com.grishko188.pointofinterest.features.main.PoiAppState
import com.grishko188.pointofinterest.features.poi.create.CreatePoiScreen

@Composable
fun Navigation(appState: PoiAppState, paddingValues: PaddingValues) {
    NavHost(appState.navController, startDestination = Screen.Home.route, Modifier.padding(paddingValues)) {
        composable(Screen.Home.route) { HomeScreen(appState.navController, appState.searchState) }
        categoriesGraph(appState)
        composable(Screen.Profile.route) { ProfileScreen(appState.navController) }
        composable(Screen.CreatePoi.route) { CreatePoiScreen(appState.navController) }
    }
}