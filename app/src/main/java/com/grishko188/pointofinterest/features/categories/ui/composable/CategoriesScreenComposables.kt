package com.grishko188.pointofinterest.features.categories.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme

@Composable
fun CategoryTypeHeader(type: String) {
    Text(
        text = type,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f))
            .padding(vertical = 4.dp, horizontal = 16.dp),
        style = MaterialTheme.typography.titleSmall,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onTertiary
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryView(modifier: Modifier = Modifier, item: CategoryUiModel, onAction: (CategoryAction, CategoryUiModel) -> Unit) {

    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onAction(CategoryAction.DELETE, item)
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colorScheme.background
                    else -> MaterialTheme.colorScheme.error
                }
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color.copy(alpha = 0.5f))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_delete_forever),
                    contentDescription = "Delete Icon",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        dismissContent = {
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .clickable { onAction(CategoryAction.EDIT, item) }) {

                Row(
                    Modifier
                        .background(Color.Transparent)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                ) {

                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(CenterVertically),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_hash_tag),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = item.title,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )


                    Spacer(modifier = Modifier.size(16.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = item.color.copy(alpha = 0.5f), shape = CircleShape)
                            .align(CenterVertically)
                    )
                }
            }
        }
    )
}


@Preview
@Composable
fun CategoriesScreenComposablesPreview() {
    PointOfInterestTheme(dynamicColor = false, darkTheme = false) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)
        ) {
            CategoryTypeHeader(type = "Priority")

            Spacer(modifier = Modifier.size(16.dp))

            val model = CategoryUiModel(id = "_ID3", title = "High!", color = Color(0xFFD50000))

            CategoryView(item = model) { action, model -> }
        }
    }
}

