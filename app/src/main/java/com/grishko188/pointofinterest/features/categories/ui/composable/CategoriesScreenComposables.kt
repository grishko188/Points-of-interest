@file:OptIn(ExperimentalMaterialApi::class)

package com.grishko188.pointofinterest.features.categories.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.SnackbarDisplayObject
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme
import com.grishko188.pointofinterest.ui.theme.WarmGray200

@Composable
fun CategoryTypeHeader(type: String) {
    Text(
        text = type,
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmGray200.copy(alpha = 0.2f))
            .padding(vertical = 4.dp, horizontal = 16.dp),
        style = MaterialTheme.typography.titleSmall,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryView(
    modifier: Modifier = Modifier,
    item: CategoryUiModel,
    onAction: (CategoryAction, CategoryUiModel, SnackbarDisplayObject?) -> Unit
) {
    val dismissStateWrapper = rememberDismissStateWithConfirmation(
        message = stringResource(id = R.string.message_snack_bar_category_deleted, item.title),
        actionTitle = stringResource(id = R.string.action_undo),
    ) { displayObject ->
        onAction(CategoryAction.DELETE, item, displayObject)
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissStateWrapper.dismissState,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.4f) },
        background = {
            val color by animateColorAsState(
                when (dismissStateWrapper.dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.error
                }
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
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
                .clickable { onAction(CategoryAction.EDIT, item, null) }) {
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

@Composable
fun rememberDismissStateWithConfirmation(
    message: String,
    actionTitle: String,
    confirmation: (SnackbarDisplayObject) -> Unit
): DismissStateWitConfirmation {
    val snackbarData = SnackbarDisplayObject(message, actionTitle)
    val dismissState = rememberDismissState {
        if (it == DismissValue.DismissedToStart) {
            confirmation(snackbarData)
            return@rememberDismissState true
        }
        return@rememberDismissState false
    }
    return remember(snackbarData, dismissState) {
        DismissStateWitConfirmation(dismissState, snackbarData)
    }
}

@OptIn(ExperimentalMaterialApi::class)
class DismissStateWitConfirmation(
    val dismissState: DismissState,
    val snackbarData: SnackbarDisplayObject
)

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

            CategoryView(item = model) { action, model, data -> }
        }
    }
}

