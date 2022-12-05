package com.grishko188.pointofinterest.features.categories.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryTypeHeader
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryView
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.vm.CategoriesViewModel

@Composable
fun CategoriesScreen(navHostController: NavHostController) {
    val viewModel = viewModel<CategoriesViewModel>()
    val categoriesState by viewModel.categoriesState.collectAsState()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        CategoriesContent(
            viewModel = viewModel,
            navigationController = navHostController,
            categories = categoriesState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    viewModel: CategoriesViewModel,
    navigationController: NavHostController,
    categories: List<CategoryUiModel>,
) {
    val context = LocalContext.current

    Column {
        LazyColumn {
            stickyHeader {
                CategoryTypeHeader(type = "Test Header")
            }
            categories.forEach { item ->
                item(key = item.hashCode()) {
                    CategoryView(Modifier.animateItemPlacement(), item) { action, model ->
                        when (action) {
                            CategoryAction.DELETE -> viewModel.onDeleteItem(model.id)
                            CategoryAction.EDIT -> Toast.makeText(context, "Edit ${model.id}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                item("Divider${item.id}") {
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}