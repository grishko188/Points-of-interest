package com.grishko188.pointofinterest.features.poi.create.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.ErrorDisplayObject
import com.grishko188.pointofinterest.features.poi.create.models.WizardSuggestionUiModel
import com.grishko188.pointofinterest.features.poi.create.vm.WizardSuggestionUiState
import com.grishko188.pointofinterest.ui.composables.uikit.PulsingProgressBar
import com.grishko188.pointofinterest.ui.theme.PointOfInterestTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WizardSuggestionStateCard(
    wizardSuggestionUiState: WizardSuggestionUiState
) {
    AnimatedContent(targetState = wizardSuggestionUiState, modifier = Modifier.padding(16.dp)) { state ->
        when (state) {
            is WizardSuggestionUiState.Loading -> WizardSuggestionLoading()
            is WizardSuggestionUiState.Error -> WizardSuggestionError(displayObject = state.displayObject)
            is WizardSuggestionUiState.Success -> WizardSuggestionContentPreview(suggestion = state.wizardSuggestionUiModel)
            is WizardSuggestionUiState.None -> WizardEmptyState()
        }
    }
}

@Composable
fun WizardEmptyState() {
}

@Composable
fun WizardSuggestionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp),
        contentAlignment = Alignment.Center

    ) {
        PulsingProgressBar(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp),
            color = MaterialTheme.colorScheme.primary,
            diameter = 64.dp
        )
    }
}

@Composable
fun WizardSuggestionError(displayObject: ErrorDisplayObject) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Icon(
            modifier = Modifier
                .size(48.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
            contentDescription = "Error icon",
            tint = MaterialTheme.colorScheme.onError
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column {
            Text(
                text = stringResource(R.string.title_ui_state_error),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onError,
                maxLines = 1
            )
            Text(
                text = stringResource(id = displayObject.errorMessage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onError,
            )
        }

    }
}

@Composable
fun WizardSuggestionContentPreview(suggestion: WizardSuggestionUiModel) {
    ElevatedCard(
        onClick = {},
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(),
        enabled = false,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground
        )

    ) {

        if (suggestion.isEmpty()) {

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .size(64.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_empty),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Text(
                        text = stringResource(id = R.string.title_wizard_suggestion_not_available),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.subtitle_wizard_suggestion_not_available),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        } else if (suggestion.isSingleImageSuggestion()) {

            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .clip(RoundedCornerShape(8.dp)),
                model = suggestion.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "Wizard suggestion image preview",
                loading = { PulsingProgressBar() },
                error = {
                    Box {
                        Icon(
                            modifier = Modifier.size(128.dp).align(Alignment.Center),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_loading_failed),
                            contentDescription = "Wizard suggestion image preview - error",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    }
                }
            )

        } else {

            Column {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (suggestion.imageUrl.isNullOrEmpty().not()) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = suggestion.imageUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Wizard suggestion image preview",
                            loading = { PulsingProgressBar() },
                            error = {
                                Box {
                                    Icon(
                                        modifier = Modifier.size(64.dp).align(Alignment.Center),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_loading_failed),
                                        contentDescription = "Wizard suggestion image preview - error",
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    Column {
                        if (suggestion.title != null) {
                            Text(
                                text = requireNotNull(suggestion.title),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        if (suggestion.body != null)
                            Text(
                                text = suggestion.body,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )

                        Spacer(modifier = Modifier.height(16.dp))
                        if (suggestion.tags != null) {

                            FlowRow(mainAxisSpacing = 4.dp, crossAxisSpacing = 2.dp) {
                                suggestion.tags.forEach { tag ->
                                    Text(

                                        text = "# $tag",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WizardSuggestionStateCardPreview() {
    PointOfInterestTheme {
        WizardSuggestionError(displayObject = ErrorDisplayObject.GenericError)

        Spacer(modifier = Modifier.size(16.dp))

        val suggestionFull = WizardSuggestionUiModel(
            title = "Test title",
            body = "Much longer text here to test body of suggestion, much longer text here to test body of suggestion",
            tags = arrayListOf("tag1", "tag2", "tag3"),
            imageUrl = "https://cdn.pixabay.com/photo/2014/02/27/16/10/flowers-276014__340.jpg"
        )

        WizardSuggestionContentPreview(suggestion = suggestionFull)
    }
}