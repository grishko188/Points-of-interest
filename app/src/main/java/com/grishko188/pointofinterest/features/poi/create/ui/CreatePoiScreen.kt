package com.grishko188.pointofinterest.features.poi.create.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.flowlayout.FlowRow
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.validator.rememberUrlValidator
import com.grishko188.pointofinterest.features.categories.ui.composable.CategoryTypeHeader
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.ui.models.toTitle
import com.grishko188.pointofinterest.features.home.ui.composable.AddMoreButton
import com.grishko188.pointofinterest.features.home.ui.composable.CategoryFilterChips
import com.grishko188.pointofinterest.features.poi.create.models.FormImageState
import com.grishko188.pointofinterest.features.poi.create.models.WizardSuggestionUiModel
import com.grishko188.pointofinterest.features.poi.create.ui.composable.ImageContentStateCard
import com.grishko188.pointofinterest.features.poi.create.ui.composable.WizardSuggestionStateCard
import com.grishko188.pointofinterest.features.poi.create.vm.CreatePoiScreenState
import com.grishko188.pointofinterest.features.poi.create.vm.CreatePoiViewModel
import com.grishko188.pointofinterest.features.poi.create.vm.WizardSuggestionUiState
import com.grishko188.pointofinterest.ui.composables.uikit.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun CreatePoiScreen(
    onCloseScreen: () -> Unit,
    viewModel: CreatePoiViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.finishScreen.collect { state ->
            if (state) onCloseScreen()
        }
    }

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (screenState.value !is CreatePoiScreenState.Loading) {
        CrossSlide(targetState = screenState.value) { state ->
            if (state is CreatePoiScreenState.Wizard) {
                CreatePoiWizardScreen(
                    sharedContent = state.sharedContent.content,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController
                )
            } else if (state is CreatePoiScreenState.Form) {
                CreatePoiFormScreen(
                    wizardSuggestionUiModel = state.suggestion,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun CreatePoiWizardScreen(
    sharedContent: String?,
    viewModel: CreatePoiViewModel,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    var wizardTextState by remember { mutableStateOf(TextFieldValue(sharedContent ?: "")) }
    val wizardSuggestionUiState by viewModel.wizardSuggestionState.collectAsStateWithLifecycle()
    val urlValidator = rememberUrlValidator()

    LaunchedEffect(key1 = sharedContent) {
        if (sharedContent != null && urlValidator.validate(sharedContent)) {
            viewModel.onFetchWizardSuggestion(sharedContent)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_wizard),
                contentDescription = "Wizard icon",
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            PoiOutlineTextField(
                modifier = Modifier.padding(16.dp),
                textFieldValue = wizardTextState,
                onValueChanged = { value -> wizardTextState = value },
                onValidValue = { viewModel.onFetchWizardSuggestion(it.text) },
                validator = urlValidator,
                focusRequester = focusRequester,
                leadingIconRes = R.drawable.ic_insert_link,
                labelTextRes = R.string.title_wizard_link_title,
                placeholderTextRes = R.string.title_wizard_link_hint,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.None),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if (urlValidator.validate(wizardTextState.text)) {
                        viewModel.onFetchWizardSuggestion(wizardTextState.text)
                    }
                })
            )

            Spacer(modifier = Modifier.size(8.dp))

            WizardSuggestionStateCard(wizardSuggestionUiState = wizardSuggestionUiState)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(color = MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                text = stringResource(id = R.string.button_skip),
                fontSize = 18.sp,
                onClick = { viewModel.onSkip() }
            )
            ActionButton(
                text = stringResource(id = R.string.button_apply),
                enabled = wizardSuggestionUiState is WizardSuggestionUiState.Success,
                fontSize = 18.sp,
                onClick = { viewModel.onApplyWizardSuggestion() }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun CreatePoiFormScreen(
    wizardSuggestionUiModel: WizardSuggestionUiModel,
    viewModel: CreatePoiViewModel,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    var contentUrlTextState by remember { mutableStateOf(TextFieldValue(wizardSuggestionUiModel.url ?: "")) }
    var titleTextState by remember { mutableStateOf(TextFieldValue(wizardSuggestionUiModel.title ?: "")) }
    var bodyTextState by remember { mutableStateOf(TextFieldValue(wizardSuggestionUiModel.body ?: "")) }
    var imageState by remember {
        mutableStateOf(
            FormImageState(imagePath = wizardSuggestionUiModel.imageUrl, isFailedImage = false)
        )
    }
    val categoriesListState = remember { mutableStateListOf<CategoryUiModel>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.toString()?.let { nonNullUri ->
                imageState = imageState.copy(imagePath = nonNullUri, isFailedImage = false)
            }
        }
    )

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetElevation = 16.dp,
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
        sheetContent = {
            CategoriesSelectionBottomSheet(
                viewModel = viewModel,
                selectedCategories = categoriesListState,
                onCategorySelected = { categoriesListState.add(it) },
                onCategoryUnselected = { categoriesListState.remove(it) }
            )
        }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
            ) {

                PoiOutlineTextField(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    textFieldValue = contentUrlTextState,
                    onValueChanged = { value -> contentUrlTextState = value },
                    onValidValue = { },
                    validator = null,
                    focusRequester = focusRequester,
                    labelTextRes = R.string.title_wizard_link_title,
                    placeholderTextRes = R.string.title_wizard_link_hint,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.None)
                )

                ImageContentStateCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    imageState = imageState,
                    focusRequester = focusRequester,
                    keyboardController = keyboardController,
                    onImageSelected = { path -> imageState = imageState.copy(imagePath = path, isFailedImage = false) },
                    onDeleteImage = { imageState = imageState.copy(imagePath = null, isFailedImage = false) },
                    onImageFailedToDownload = { imageState = imageState.copy(isFailedImage = true) },
                    onPickLocalImage = {
                        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )

                val addCategoryTitle = if (categoriesListState.size == 0) stringResource(id = R.string.button_add_categories)
                else stringResource(id = R.string.add_more)

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 2.dp
                ) {
                    categoriesListState.forEach { model -> CategoryFilterChips(model) }
                    AddMoreButton(text = addCategoryTitle) {
                        coroutineScope.launch { bottomSheetState.show() }
                    }
                }

                PoiOutlineTextField(
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    textFieldValue = titleTextState,
                    onValueChanged = { value -> titleTextState = value },
                    onValidValue = { },
                    validator = null,
                    maxLines = 3,
                    focusRequester = focusRequester,
                    labelTextRes = R.string.title_form_title
                )

                PoiOutlineTextField(
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    textFieldValue = bodyTextState,
                    onValueChanged = { value -> bodyTextState = value },
                    onValidValue = { },
                    validator = null,
                    maxLines = 10,
                    focusRequester = focusRequester,
                    labelTextRes = R.string.title_form_body
                )
            }

            val isSaveEnabled = titleTextState.text.isNotEmpty()
                    && categoriesListState.isNotEmpty()
                    && (bodyTextState.text.isNotEmpty()
                    || contentUrlTextState.text.isNotEmpty()
                    || (imageState.imagePath.isNullOrEmpty().not() && imageState.isFailedImage.not()))

            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                shadowElevation = 8.dp
            ) {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(id = R.string.button_save),
                    enabled = isSaveEnabled,
                    onClick = {
                        viewModel.onSave(
                            contentUrl = contentUrlTextState.text,
                            title = titleTextState.text,
                            body = bodyTextState.text,
                            imageState = imageState,
                            categories = categoriesListState
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CategoriesSelectionBottomSheet(
    viewModel: CreatePoiViewModel,
    selectedCategories: List<CategoryUiModel>,
    onCategorySelected: (CategoryUiModel) -> Unit,
    onCategoryUnselected: (CategoryUiModel) -> Unit
) {
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
            )
            .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .width(32.dp)
                    .height(8.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
            )
            LazyColumn {
                categoriesState.entries.forEach { entry ->
                    item(entry.key) {
                        CategoryTypeHeader(type = stringResource(id = entry.key.toTitle()))
                    }
                    item("group${entry.key}") {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                            mainAxisSpacing = 8.dp,
                            crossAxisSpacing = 2.dp
                        ) {
                            entry.value.forEach { model ->
                                CategoryFilterChips(
                                    categoryListItem = model,
                                    isSelected = model in selectedCategories,
                                    onClick = {
                                        if (model !in selectedCategories) {
                                            onCategorySelected(model)
                                        } else {
                                            onCategoryUnselected(model)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}