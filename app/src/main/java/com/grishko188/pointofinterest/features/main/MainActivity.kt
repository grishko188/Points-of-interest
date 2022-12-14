package com.grishko188.pointofinterest.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.grishko188.pointofinterest.features.main.vm.MainUiState
import com.grishko188.pointofinterest.features.main.vm.MainViewModel
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            when (mainViewModel.uiState.value) {
                MainUiState.Loading -> true
                else -> false
            }
        }
        lifecycle.addObserver(mainViewModel)

        setContent {
            PointOfInterestTheme(dynamicColor = false) { PoiMainScreen() }
        }
    }
}