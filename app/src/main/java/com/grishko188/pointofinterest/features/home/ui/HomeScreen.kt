package com.grishko188.pointofinterest.features.home.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.containsId
import com.grishko188.pointofinterest.features.home.ui.composable.CategoryFilterChips
import com.grishko188.pointofinterest.features.home.ui.composable.PoiCard
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.home.ui.composable.AddMoreButton
import com.grishko188.pointofinterest.features.home.ui.models.PoiListItem
import com.grishko188.pointofinterest.features.home.vm.HomeViewModel
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.ui.composables.uistates.EmptyView
import com.grishko188.pointofinterest.ui.composables.uistates.ErrorView
import com.grishko188.pointofinterest.ui.composables.uistates.ProgressView

@Composable
fun HomeScreen(
    navigationController: NavHostController,
) {
    val viewModel = viewModel<HomeViewModel>()
    val homeContentState by viewModel.homeUiContentState.collectAsState()
    val categoriesState by viewModel.categoriesState.collectAsState()
    var selectedFiltersState by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    Column(
        Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = categoriesState.isEmpty().not(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            HomeScreenFilterContent(
                navigationController,
                selectedFilters = selectedFiltersState,
                categories = categoriesState
            ) { filterId ->
                selectedFiltersState = selectedFiltersState.toMutableList().apply {
                    if (filterId in selectedFiltersState) remove(filterId)
                    else add(filterId)
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            when (homeContentState) {
                HomeViewModel.HomeUiContentState.Loading -> ProgressView()

                HomeViewModel.HomeUiContentState.Empty -> EmptyView(message = stringResource(id = R.string.message_ui_state_empty_main_screen))

                is HomeViewModel.HomeUiContentState.Error -> {
                    val errorState = homeContentState as HomeViewModel.HomeUiContentState.Error
                    ErrorView(message = errorState.message) { viewModel.onRetry() }
                }

                is HomeViewModel.HomeUiContentState.Result -> {
                    val filteredList = (homeContentState as HomeViewModel.HomeUiContentState.Result).poiList.filter { poi ->
                        selectedFiltersState.isEmpty() || selectedFiltersState.all { filterId -> poi.categories.containsId(filterId) }
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = filteredList.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        EmptyView(message = stringResource(id = R.string.message_ui_state_empty_main_screen_no_filters))
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = categoriesState.isEmpty().not(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        HomeScreenContent(filteredList, navigationController)
                    }

                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    poiItems: List<PoiListItem>,
    navigationController: NavHostController
) {
    Column {
        LazyColumn {
            poiItems.forEachIndexed { index, item ->
                item(key = item.hashCode()) {
                    PoiCard(poiListItem = item, onClick = { navigationController.navigate(Screen.CreatePoi.route) })
                }
                if (index < poiItems.size - 1) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
fun HomeScreenFilterContent(
    navigationController: NavHostController,
    categories: List<CategoryUiModel>,
    selectedFilters: List<String>,
    onClick: (String) -> Unit
) {
    Column {

        LazyRow {
            categories.forEach { item ->
                item(key = item.hashCode()) {
                    CategoryFilterChips(
                        categoryListItem = item,
                        onClick = { onClick(it.id) },
                        isSelected = item.id in selectedFilters
                    )
                }
                item { Spacer(modifier = Modifier.width(8.dp)) }
            }

            item {
                AddMoreButton {
                    navigationController.navigate(Screen.Categories.route)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}