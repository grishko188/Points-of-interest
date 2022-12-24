package com.grishko188.pointofinterest.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.grishko188.pointofinterest.features.main.vm.MainScreenState
import com.grishko188.pointofinterest.features.main.vm.SyncStateState
import com.grishko188.pointofinterest.features.main.vm.MainViewModel
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var navHostController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var mainState: MainScreenState by mutableStateOf(MainScreenState.Loading)
        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainViewModel.mainScreenState
                    .onEach {
                        mainState = it
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.syncState.value == SyncStateState.Loading && mainState == MainScreenState.Loading
        }

        lifecycle.addObserver(mainViewModel)

        setContent {
            PointOfInterestTheme(
                useSystemTheme = mainState.userSettings()?.isUseSystemTheme ?: true,
                darkTheme = mainState.userSettings()?.isDarkMode ?: true
            ) {
                navHostController = rememberNavController()
                val appState = rememberPoiAppState(navController = requireNotNull(navHostController))
                PoiMainScreen(appState)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostController?.handleDeepLink(intent)
    }

    private fun MainScreenState.userSettings() = if (this is MainScreenState.Result) userSettings else null
}