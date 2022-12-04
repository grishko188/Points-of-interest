package com.grishko188.pointofinterest.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.models.UiState
import com.grishko188.pointofinterest.features.home.vm.HomeViewModel
import com.grishko188.pointofinterest.ui.composables.uistates.EmptyView
import com.grishko188.pointofinterest.ui.composables.uistates.ErrorView
import com.grishko188.pointofinterest.ui.composables.uistates.ProgressView

@Composable
fun HomeScreen(
    navigationController: NavHostController,
) {
    val viewModel = viewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.onLaunched()
    }

    when (uiState) {
        UiState.Loading -> ProgressView()
        UiState.Empty -> EmptyView(message = stringResource(id = R.string.message_ui_state_empty_main_screen))
        is UiState.Error -> {
            val errorState = uiState as UiState.Error
            ErrorView(message = errorState.message) { viewModel.onRetry() }
        }
        else -> HomeScreenContent()
    }
}

@Composable
fun HomeScreenContent() {
    Box(
        Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Content",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}