package com.grishko188.pointofinterest.features.categories.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.stringFromResource
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryTypeHeader
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryView
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.vm.CategoriesViewModel
import com.grishko188.pointofinterest.ui.composables.uikit.PrimaryButton

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
    categories: Map<String, List<CategoryUiModel>>,
) {
    val context = LocalContext.current

    Column {
        LazyColumn(Modifier.weight(1f)) {
            categories.entries.forEach { group ->
                stickyHeader {
                    CategoryTypeHeader(type = group.key)
                }
                items(group.value, { it.hashCode() }) { item ->
                    CategoryView(Modifier.animateItemPlacement(), item) { action, model ->
                        when (action) {
                            CategoryAction.DELETE -> viewModel.onDeleteItem(model.id)
                            CategoryAction.EDIT -> Toast.makeText(context, "Edit ${model.id}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Divider(
                        modifier = Modifier.animateItemPlacement(),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.button_create_new),
                onClick = { Toast.makeText(context, "Create new category", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}