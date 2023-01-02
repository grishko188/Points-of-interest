package com.grishko188.pointofinterest.features.search.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.features.home.ui.HomeScreenContent
import com.grishko188.pointofinterest.features.main.PoiAppState
import com.grishko188.pointofinterest.features.search.vm.SearchScreenUiState
import com.grishko188.pointofinterest.features.search.vm.SearchVm
import com.grishko188.pointofinterest.navigation.Screen

@OptIn(ExperimentalAnimationApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(
    appState: PoiAppState,
    viewModel: SearchVm = hiltViewModel()
) {
    val searchState = remember { appState.searchState }

    val emptySearchState by remember { derivedStateOf { searchState.value.text.isEmpty() } }
    val searchScreenState by viewModel.searchScreenState.collectAsStateWithLifecycle()
    val onNavigate: (Screen, List<Pair<String, Any>>) -> Unit = { screen, args ->
        appState.closeSearch()
        appState.navigateTo(screen, args)
    }

    BackHandler(appState.showSearchBarState) {
        appState.closeSearch()
    }

    LaunchedEffect(key1 = searchState.value) {
        viewModel.onSearch(searchState.value.text)
    }

    AnimatedContent(
        modifier = Modifier.padding(16.dp),
        targetState = emptySearchState to searchScreenState
    ) { state ->
        if (state.first) {
            EmptySearch(stringResource(id = R.string.message_enter_search_request))
        } else if (state.second is SearchScreenUiState.NothingFound) {
            EmptySearch(stringResource(id = R.string.message_search_empty_result))
        } else if (state.second is SearchScreenUiState.SearchResult) {
            HomeScreenContent((state.second as SearchScreenUiState.SearchResult).result) { id ->
                onNavigate(
                    Screen.ViewPoiDetailed,
                    listOf(Screen.ViewPoiDetailed.ARG_POI_ID to id)
                )
            }
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