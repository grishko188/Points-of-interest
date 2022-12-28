package com.grishko188.pointofinterest.features.poi.create.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.extractSource
import com.grishko188.pointofinterest.core.utils.isLocalImageUri
import com.grishko188.pointofinterest.core.utils.isRemoteImageUri
import com.grishko188.pointofinterest.core.validator.rememberUrlValidator
import com.grishko188.pointofinterest.features.poi.create.models.FormImageState
import com.grishko188.pointofinterest.ui.composables.uikit.PoiOutlineTextField
import com.grishko188.pointofinterest.ui.composables.uikit.PulsingProgressBar

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ImageContentStateCard(
    modifier: Modifier = Modifier,
    imageState: FormImageState,
    keyboardController: SoftwareKeyboardController?,
    focusRequester: FocusRequester,
    onImageSelected: (String) -> Unit,
    onDeleteImage: () -> Unit,
    onImageFailedToDownload: () -> Unit,
    onPickLocalImage: () -> Unit
) {
    AnimatedContent(targetState = imageState, modifier = modifier) { state ->
        if (state.imagePath.isNullOrEmpty().not()) {
            DisplayImageState(
                imagePath = requireNotNull(state.imagePath),
                onImageFailedToDownload = onImageFailedToDownload,
                onDeleteImage = onDeleteImage
            )
        } else {
            EmptyImageState(
                keyboardController = keyboardController,
                focusRequester = focusRequester,
                onImageSelected = onImageSelected,
                onPickLocalImage = onPickLocalImage
            )
        }
    }
}

@Composable
fun DisplayImageState(
    imagePath: String,
    onImageFailedToDownload: () -> Unit,
    onDeleteImage: () -> Unit
) {
    var shouldShowSourceState: Boolean by remember { mutableStateOf(false) }

    val source = if (imagePath.isLocalImageUri()) {
        stringResource(id = R.string.title_source_local_image)
    } else if (imagePath.isRemoteImageUri()) {
        stringResource(id = R.string.title_source_remote_image, imagePath.extractSource().toString())
    } else {
        null
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
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(8.dp)),
            model = imagePath,
            contentScale = ContentScale.Crop,
            contentDescription = "Poi image preview",
            onLoading = { shouldShowSourceState = false },
            onError = { onImageFailedToDownload() },
            onSuccess = { shouldShowSourceState = true },
            loading = { PulsingProgressBar() },
            error = {
                Box {
                    Icon(
                        modifier = Modifier.size(128.dp).align(Alignment.Center),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_loading_failed),
                        contentDescription = "Wizard suggestion image preview - error",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                    )
                }
            }
        )
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .size(36.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), shape = CircleShape)
                .align(Alignment.TopEnd)
                .clip(CircleShape),
            onClick = onDeleteImage
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete_forever),
                contentDescription = "Delete image button",
                tint = MaterialTheme.colorScheme.background
            )
        }
        if (source != null && shouldShowSourceState) {
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmptyImageState(
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController?,
    focusRequester: FocusRequester,
    onImageSelected: (String) -> Unit,
    onPickLocalImage: () -> Unit
) {
    var remoteImageState by remember { mutableStateOf(TextFieldValue("")) }
    val urlValidator = rememberUrlValidator()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        PoiOutlineTextField(
            modifier = Modifier.weight(1f),
            textFieldValue = remoteImageState,
            onValueChanged = { value -> remoteImageState = value },
            onValidValue = { },
            focusRequester = focusRequester,
            validator = urlValidator,
            labelTextRes = R.string.title_form_remote_image_url,
            placeholderTextRes = R.string.title_wizard_link_hint,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.None),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                if (urlValidator.validate(remoteImageState.text)) {
                    onImageSelected(remoteImageState.text)
                }
            })
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = stringResource(id = R.string.title_form_or),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.size(16.dp))

        IconButton(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(56.dp)
                .aspectRatio(1f)
                .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            onClick = onPickLocalImage
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_icon),
                contentDescription = "Select local image",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}