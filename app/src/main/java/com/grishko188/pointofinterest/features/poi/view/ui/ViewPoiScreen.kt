package com.grishko188.pointofinterest.features.poi.view.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.utils.chromeTabsIntent
import com.grishko188.pointofinterest.core.utils.launch
import com.grishko188.pointofinterest.features.main.OnMenuItemListener
import com.grishko188.pointofinterest.features.main.PoiAppState
import com.grishko188.pointofinterest.features.poi.view.models.PoiDetailListItem
import com.grishko188.pointofinterest.features.poi.view.vm.ViewPoiVm
import com.grishko188.pointofinterest.navigation.MenuActionType
import com.grishko188.pointofinterest.ui.composables.uikit.ActionButton
import com.grishko188.pointofinterest.ui.composables.uikit.PoiFilledTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ViewPoiScreen(
    appState: PoiAppState,
    onCloseScreen: () -> Unit,
    viewModel: ViewPoiVm = hiltViewModel()
) {
    LaunchedEffect(key1 = viewModel.finishScreenState) {
        viewModel.finishScreenState.collect { event ->
            if (event) onCloseScreen()
        }
    }

    var showDeleteConfirmationState by remember { mutableStateOf(false) }
    var commentTextState by remember { mutableStateOf(TextFieldValue("")) }
    val onCloseDialog = { showDeleteConfirmationState = false }
    val chromeTabsIntent = chromeTabsIntent()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val lazyColumnsState = rememberLazyListState()
    val onLinkClicked: (String) -> Unit = { link ->
        chromeTabsIntent.launch(context, link)
    }

    LaunchedEffect(key1 = true) {
        appState.registerMenuItemClickObserver(MenuActionType.DELETE, object : OnMenuItemListener {
            override fun onMenuItemClicked(menuActionType: MenuActionType) {
                showDeleteConfirmationState = true
            }
        })
    }

    DisposableEffect(key1 = true) {
        onDispose { appState.disposeMenuItemObserver(MenuActionType.DELETE) }
    }

    val mainUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val itemsToDeleteState by viewModel.itemToDeleteState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyColumnsState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            items(mainUiState.filter { it.uniqueKey !in itemsToDeleteState }, key = { item -> item.uniqueKey }) { item ->
                when (item) {
                    is PoiDetailListItem.TitleItem -> PoiTitle(title = item.text)
                    is PoiDetailListItem.ImageItem -> PoiDetailedImage(imagePath = item.imageUrl)
                    is PoiDetailListItem.MetadataItem -> PoiMetadata(dataTime = item.dateTime)
                    is PoiDetailListItem.CategoriesItem -> PoiCategories(categories = item.categoryUiModel)
                    is PoiDetailListItem.BodyItem -> PoiDescription(body = item.text, onLinkClicked)
                    is PoiDetailListItem.ContentUrl -> {
                        PoiContentLink(source = item.source, contentLink = item.url, onClick = onLinkClicked)
                    }
                    is PoiDetailListItem.CommentsCount -> {
                        PoiCommentsCount(count = item.count)
                    }
                    is PoiDetailListItem.CommentItem -> {
                        PoiComment(
                            modifier = Modifier.animateItemPlacement(),
                            id = item.id,
                            message = item.body,
                            dateTime = item.dateTime,
                            shouldShowDivider = item.shouldShowDivider,
                            onLinkClicked = onLinkClicked,
                            onDeleteComment = { id, displayObject ->
                                viewModel.onDeleteComment(id)
                                displayObject.let { snackbarDisplayData ->
                                    appState.coroutineScope.launch {
                                        val snackBarResult = appState.snackBarHostState.showSnackbar(
                                            message = snackbarDisplayData.message,
                                            actionLabel = snackbarDisplayData.action,
                                            duration = SnackbarDuration.Short
                                        )
                                        when (snackBarResult) {
                                            SnackbarResult.Dismissed -> viewModel.onCommitCommentDelete(id)
                                            SnackbarResult.ActionPerformed -> viewModel.onUndoDeleteComment(id)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(color = MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PoiFilledTextField(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 8.dp)
                    .weight(1f),
                textFieldValue = commentTextState,
                onValueChanged = { value -> commentTextState = value },
                onValidValue = { },
                validator = null,
                maxLines = 3,
                focusRequester = focusRequester,
                placeholderTextRes = R.string.title_add_comment,
            )

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                enabled = commentTextState.text.isNotEmpty(),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(CircleShape),
                onClick = {
                    keyboardController?.hide()
                    viewModel.onAddComment(commentTextState.text)
                    commentTextState = TextFieldValue("")
                    appState.coroutineScope.launch {
                        lazyColumnsState.scrollToItem(mainUiState.indexOf(mainUiState.last()))
                    }
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                    contentDescription = "Add comment button",
                )
            }
        }
    }

    if (showDeleteConfirmationState) {
        Dialog(
            onDismissRequest = onCloseDialog,
            content = {
                Card(
                    modifier = Modifier.fillMaxWidth(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {

                    Column(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 24.dp, top = 24.dp, start = 24.dp, bottom = 8.dp),
                            text = stringResource(id = R.string.title_dialog_delete),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            modifier = Modifier.padding(end = 24.dp, top = 24.dp, start = 24.dp, bottom = 24.dp),
                            text = stringResource(id = R.string.message_dialog_delete_poi),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.size(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ActionButton(
                                text = stringResource(id = R.string.cancel),
                                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0f),
                                onClick = { onCloseDialog() }
                            )

                            ActionButton(
                                text = stringResource(id = R.string.confirm),
                                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0f),
                                onClick = {
                                    viewModel.onDeletePoi()
                                    onCloseDialog()
                                }
                            )
                        }
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}