@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.grishko188.pointofinterest.features.categories.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grishko188.domain.features.categories.models.CategoryType
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.MaterialColors
import com.grishko188.pointofinterest.core.utils.SnackbarDisplayObject
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryAction
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.ui.theme.DarkMainColor
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme
import com.grishko188.pointofinterest.ui.theme.WarmGray200

@Composable
fun CategoryTypeHeader(type: String) {
    Text(
        text = type,
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmGray200)
            .padding(vertical = 4.dp, horizontal = 16.dp),
        style = MaterialTheme.typography.titleSmall,
        fontSize = 12.sp,
        color = DarkMainColor
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditableCategoryView(
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
            CategoryView(
                modifier = Modifier.clickable { onAction(CategoryAction.EDIT, item, null) },
                item = item
            )
        }
    )
}

@Composable
fun CategoryView(
    modifier: Modifier = Modifier,
    item: CategoryUiModel
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
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


@Composable
fun GridColorPalette(
    modifier: Modifier = Modifier,
    originalColor: Color? = null,
    selectedColor: Color? = null,
    onColorSelected: (Color) -> Unit
) {
    Column(modifier) {

        if (originalColor != null) {

            ColorPaletteItem(
                modifier = Modifier
                    .size(48.dp)
                    .testTag("original_selected_color_item"),
                color = originalColor,
                isSelected = true,
                onColorClicked = onColorSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        val count = 4
        val singleItemHeight = 48
        val spacing = 8
        val height = (singleItemHeight * count) + (spacing * (count - 1))

        LazyHorizontalGrid(
            modifier = Modifier.height(height.dp),
            rows = GridCells.Fixed(count),
            // content padding
            verticalArrangement = Arrangement.spacedBy(spacing.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.dp),
            content = {
                items(MaterialColors.flatUniquePalette, key = { color -> color.toArgb() }) { color ->
                    ColorPaletteItem(
                        modifier = Modifier.aspectRatio(1f),
                        color = color,
                        isSelected = color == selectedColor,
                        onColorClicked = onColorSelected
                    )
                }
            }
        )
    }
}

@Composable
fun ColorPaletteItem(modifier: Modifier, color: Color, isSelected: Boolean, onColorClicked: (Color) -> Unit) {
    Box(
        modifier = modifier
            .semantics { selected = isSelected }
            .background(color, shape = CircleShape)
            .clip(CircleShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), shape = CircleShape)
            .clickable(role = Role.Button) { onColorClicked(color) }
            .aspectRatio(1f)


    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f))
                    .fillMaxSize()
            )

            Icon(
                modifier = Modifier.align(Center),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_done),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview
@Composable
fun CategoriesScreenComposablesPreview() {
    PointOfInterestTheme(darkTheme = false) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)
        ) {
            CategoryTypeHeader(type = "Priority")

            Spacer(modifier = Modifier.size(16.dp))

            val model = CategoryUiModel(id = "_ID3", title = "High!", color = Color(0xFFD50000), categoryType = CategoryType.SEVERITY)

            CategoryView(item = model)

            Spacer(modifier = Modifier.size(16.dp))

            ColorPaletteItem(modifier = Modifier.size(48.dp), color = Color(0xFFD50000), isSelected = true, onColorClicked = {})

            Spacer(modifier = Modifier.size(16.dp))

            GridColorPalette(onColorSelected = {}, selectedColor = Color(0xFFD50000))
        }
    }
}

