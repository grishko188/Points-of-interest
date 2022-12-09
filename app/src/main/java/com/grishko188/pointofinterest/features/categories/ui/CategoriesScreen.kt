package com.grishko188.pointofinterest.features.categories.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryTypeHeader
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryView
import com.grishko188.pointofinterest.features.categories.ui.composable.EditableCategoryView
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.vm.CategoriesViewModel
import com.grishko188.pointofinterest.features.main.PoiAppState
import com.grishko188.pointofinterest.navigation.Screen
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    appState: PoiAppState,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    viewModel: CategoriesViewModel
) {

    val categoriesState by viewModel.categoriesState.collectAsState()
    val itemToDelete by viewModel.itemsToDelete.collectAsState()

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        CategoriesContent(
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            appState = appState,
            categories = categoriesState,
            itemsToDelete = itemToDelete
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    appState: PoiAppState,
    viewModel: CategoriesViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    categories: Map<String, List<CategoryUiModel>>,
    itemsToDelete: List<String>
) {

    Column {
        LazyColumn(Modifier.weight(1f)) {
            categories.entries.forEach { group ->
                stickyHeader {
                    CategoryTypeHeader(type = group.key)
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
                items(group.value.filter { it.id !in itemsToDelete }, { it.hashCode() }) { item ->
                    if (item.isMutableCategory) {
                        EditableCategoryView(Modifier.animateItemPlacement(), item) { action, model, displayData ->
                            when (action) {
                                CategoryAction.DELETE -> {
                                    viewModel.onDeleteItem(item.id)
                                    displayData?.let { snackbarDisplayData ->
                                        coroutineScope.launch {
                                            val snackBarResult = snackbarHostState.showSnackbar(
                                                message = snackbarDisplayData.message,
                                                actionLabel = snackbarDisplayData.action,
                                                duration = SnackbarDuration.Short
                                            )
                                            when (snackBarResult) {
                                                SnackbarResult.Dismissed -> viewModel.onCommitDeleteItem(model.id)
                                                SnackbarResult.ActionPerformed -> viewModel.onUndoDeleteItem(model.id)
                                            }
                                        }
                                    }
                                }
                                CategoryAction.EDIT -> appState.navigateTo(
                                    Screen.CategoriesDetailed,
                                    listOf(Screen.CategoriesDetailed.ARG_CATEGORY_ID to model.id)
                                )
                            }
                        }
                    } else {
                        CategoryView(Modifier.animateItemPlacement(), item = item)
                    }
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}