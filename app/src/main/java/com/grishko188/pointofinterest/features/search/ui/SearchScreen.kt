package com.grishko188.pointofinterest.features.search.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.features.main.PoiAppState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    appState: PoiAppState,
    searchQuery: TextFieldValue
) {

    BackHandler(appState.showSearchBarState) {
        appState.showSearchBarState = false
        appState.onBackClick()
    }

    LaunchedEffect(key1 = searchQuery) {

    }

    AnimatedContent(targetState = searchQuery) { state ->
        if (state.text.isEmpty()) {
            EmptySearch(stringResource(id = R.string.message_enter_search_request))
        } else {
            EmptySearch(message = state.text)
        }
    }
}

@Composable
fun EmptySearch(message: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            maxLines = 1
        )
    }
}