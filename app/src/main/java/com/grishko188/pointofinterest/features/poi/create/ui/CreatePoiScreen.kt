package com.grishko188.pointofinterest.features.poi.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.navigation.NavHostController
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.core.validator.rememberUrlValidator
import com.grishko188.pointofinterest.features.poi.create.models.WizardSuggestionUiModel
import com.grishko188.pointofinterest.features.poi.create.ui.composable.WizardSuggestionStateCard
import com.grishko188.pointofinterest.features.poi.create.vm.CreatePoiScreenState
import com.grishko188.pointofinterest.features.poi.create.vm.CreatePoiViewModel
import com.grishko188.pointofinterest.features.poi.create.vm.WizardSuggestionUiState
import com.grishko188.pointofinterest.ui.composables.uikit.ActionButton
import com.grishko188.pointofinterest.ui.composables.uikit.CrossSlide
import com.grishko188.pointofinterest.ui.composables.uistates.ProgressView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun CreatePoiScreen(
    navController: NavHostController,
    viewModel: CreatePoiViewModel = hiltViewModel()
) {

    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    viewModel.sharedContentState.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    CrossSlide(targetState = screenState.value) { state ->
        when (state) {
            is CreatePoiScreenState.Wizard -> CreatePoiWizardScreen(
                sharedContent = state.sharedContent.content,
                viewModel = viewModel,
                focusRequester = focusRequester,
                keyboardController = keyboardController
            )
            is CreatePoiScreenState.Form -> CreatePoiFormScreen(
                wizardSuggestionUiModel = state.suggestion,
                viewModel = viewModel,
                focusRequester = focusRequester,
                keyboardController = keyboardController
            )
            else -> ProgressView()
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
    viewModel.searchState.collectAsStateWithLifecycle()
    val urlValidator = rememberUrlValidator()

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

            OutlinedTextField(
                value = wizardTextState,
                onValueChange = { value ->
                    wizardTextState = value
                    if (urlValidator.validate(value.text)) {
                        viewModel.onFetchWizardSuggestion(value.text)
                    }
                },
                isError = urlValidator.isValid.not(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.bodyLarge,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_insert_link),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                trailingIcon = {
                    if (wizardTextState.text.isNotEmpty()) {
                        IconButton(
                            onClick = { wizardTextState = TextFieldValue("") }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), shape = CircleShape),
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                },
                singleLine = true,
                maxLines = 1,
                shape = RoundedCornerShape(8.dp),
                label = { Text(text = stringResource(id = R.string.title_wizard_link_title)) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_wizard_link_hint),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                },
                supportingText = {
                    if (urlValidator.isValid.not()) {
                        Text(
                            text = urlValidator.getErrorMessage(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePoiFormScreen(
    wizardSuggestionUiModel: WizardSuggestionUiModel,
    viewModel: CreatePoiViewModel,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {


}