package com.grishko188.pointofinterest.features.poi.view.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.SnackbarDisplayObject
import com.grishko188.pointofinterest.core.utils.extractSource
import com.grishko188.pointofinterest.core.utils.isRemoteImageUri
import com.grishko188.pointofinterest.features.categories.ui.composable.rememberDismissStateWithConfirmation
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.home.ui.composable.CategoryFilterChips
import com.grishko188.pointofinterest.ui.composables.uikit.LinkifyText
import com.grishko188.pointofinterest.ui.composables.uikit.PulsingProgressBar

@Composable
fun PoiDetailedImage(imagePath: String) {

    val source = if (imagePath.isRemoteImageUri()) {
        stringResource(id = R.string.title_source_remote_image, imagePath.extractSource().toString())
    } else {
        stringResource(id = R.string.title_source_local_image)
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model = imagePath,
            contentScale = ContentScale.FillWidth,
            contentDescription = "Poi image preview",
            loading = { PulsingProgressBar() },
            error = {
                Box {
                    Icon(
                        modifier = Modifier
                            .size(128.dp)
                            .align(Alignment.Center),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_loading_failed),
                        contentDescription = "Wizard suggestion image preview - error",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                    )
                }
            }
        )
        val shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), shape = shape)
                .align(Alignment.BottomStart)
                .clip(shape)
                .padding(8.dp),
            text = source,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.background,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun PoiMetadata(dataTime: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = dataTime,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun PoiCategories(categories: List<CategoryUiModel>) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 2.dp
    ) {
        categories.forEach { model -> CategoryFilterChips(model) }
    }
}

@Composable
fun PoiTitle(title: String) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 3
    )
}

@Composable
fun PoiDescription(body: String, onLinkClicked: (String) -> Unit) {
    LinkifyText(
        modifier = Modifier.padding(top = 16.dp),
        linkColor = MaterialTheme.colorScheme.primary,
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        onClickLink = onLinkClicked
    )
}

@Composable
fun PoiContentLink(source: String, contentLink: String, onClick: (String) -> Unit) {
    ElevatedCard(
        onClick = { onClick(contentLink) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(64.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                text = source,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textDecoration = TextDecoration.Underline
            )

            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_open_link),
                contentDescription = "Open web url icon"
            )
        }
    }
}

@Composable
fun PoiCommentsCount(count: Int) {
    Row(
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.title_comments_count),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }

    Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PoiComment(
    modifier: Modifier = Modifier,
    id: String,
    message: String,
    dateTime: String,
    shouldShowDivider: Boolean,
    onDeleteComment: (String, SnackbarDisplayObject) -> Unit,
    onLinkClicked: (String) -> Unit
) {
    val dismissStateWrapper = rememberDismissStateWithConfirmation(
        message = stringResource(id = R.string.message_snack_bar_comment_deleted),
        actionTitle = stringResource(id = R.string.action_undo),
    ) { displayObject ->
        onDeleteComment(id, displayObject)
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissStateWrapper.dismissState,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = {
            androidx.compose.material.FractionalThreshold(0.5f)
        },
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
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp, bottom = 16.dp),
            ) {
                LinkifyText(
                    modifier = Modifier.fillMaxWidth(),
                    text = message,
                    linkColor = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    onClickLink = onLinkClicked
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    text = dateTime,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                )
            }

            if (shouldShowDivider)
                Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
        }
    )
}