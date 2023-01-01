package com.grishko188.pointofinterest.features.poi.create.vm

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.grishko188.domain.features.categories.interactor.GetCategoriesUseCase
import com.grishko188.domain.features.poi.interactor.CreatePoiUseCase
import com.grishko188.domain.features.poi.interactor.GetWizardSuggestionUseCase
import com.grishko188.domain.features.poi.models.PoiCreationPayload
import com.grishko188.pointofinterest.core.utils.ErrorDisplayObject
import com.grishko188.pointofinterest.core.utils.toDisplayObject
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.grishko188.pointofinterest.features.categories.ui.models.toDomainModel
import com.grishko188.pointofinterest.features.categories.ui.models.toUiModel
import com.grishko188.pointofinterest.features.poi.create.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePoiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWizardSuggestionUseCase: GetWizardSuggestionUseCase,
    private val createPoiUseCase: CreatePoiUseCase,
    getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _finishScreen = MutableSharedFlow<Boolean>()
    val finishScreen = _finishScreen.asSharedFlow()

    private val _screenState = MutableStateFlow<CreatePoiScreenState>(CreatePoiScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _wizardSuggestionState = MutableStateFlow<WizardSuggestionUiState>(WizardSuggestionUiState.None)
    val wizardSuggestionState = _wizardSuggestionState.asStateFlow()

    val categoriesState = getCategoriesUseCase(Unit)
        .map { list -> list.map { it.toUiModel() }.groupBy { it.categoryType } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    private val queryState = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    private val searchState = queryState
        .onEach { if (it.isEmpty()) _wizardSuggestionState.value = WizardSuggestionUiState.None }
        .filter { it.isNotEmpty() }
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapConcat { url ->
            channelFlow {
                send(getWizardSuggestionUseCase(GetWizardSuggestionUseCase.Params(url)))
            }.onStart {
                _wizardSuggestionState.value = WizardSuggestionUiState.Loading
            }.catch {
                _wizardSuggestionState.value = WizardSuggestionUiState.Error(it.toDisplayObject())
            }
        }
        .onEach {
            _wizardSuggestionState.value = WizardSuggestionUiState.Success(it.toUiModel())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val sharedContentState = savedStateHandle.getStateFlow(NavController.KEY_DEEP_LINK_INTENT, Intent())
        .map { intent -> intent.fetchSharedContent() }
        .onEach {
            if (it.contentType == ContentType.MANUAL || it.contentType == ContentType.URL) {
                _screenState.value = CreatePoiScreenState.Wizard(it)
            } else {
                _screenState.value = CreatePoiScreenState.Form(it.toWizardSuggestion())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SharedContent.EMPTY
        )

    init {
        sharedContentState.launchIn(viewModelScope)
        searchState.launchIn(viewModelScope)
    }

    fun onApplyWizardSuggestion() {
        (wizardSuggestionState.value as? WizardSuggestionUiState.Success)?.wizardSuggestionUiModel?.let { suggestionModel ->
            _screenState.value = CreatePoiScreenState.Form(suggestionModel)
        }
    }

    fun onSkip() {
        val sharedContent = (screenState.value as CreatePoiScreenState.Wizard).sharedContent
        _screenState.value = CreatePoiScreenState.Form(WizardSuggestionUiModel(url = sharedContent.content))
    }

    fun onSave(
        contentUrl: String?,
        title: String,
        body: String?,
        imageState: FormImageState,
        categories: List<CategoryUiModel>
    ) {
        viewModelScope.launch {
            val payload = PoiCreationPayload(
                contentLink = contentUrl,
                title = title,
                body = body,
                imageUrl = imageState.takeIf { imageState.isFailedImage.not() }?.imagePath,
                categories = categories.map { it.toDomainModel() }
            )

            createPoiUseCase(CreatePoiUseCase.Params(payload))

            _finishScreen.emit(true)
        }
    }

    fun onFetchWizardSuggestion(url: String) {
        queryState.value = url
    }

    private fun SharedContent.toWizardSuggestion() = when (this.contentType) {
        ContentType.LOCAL_IMAGE -> WizardSuggestionUiModel(imageUrl = content)
        ContentType.PLAIN_TEXT -> WizardSuggestionUiModel(body = content)
        else -> WizardSuggestionUiModel.EMPTY
    }
}

sealed class CreatePoiScreenState {
    data class Wizard(val sharedContent: SharedContent) : CreatePoiScreenState()
    data class Form(val suggestion: WizardSuggestionUiModel) : CreatePoiScreenState()
    object Loading : CreatePoiScreenState()
}

sealed class WizardSuggestionUiState {
    data class Success(val wizardSuggestionUiModel: WizardSuggestionUiModel) : WizardSuggestionUiState()
    object Loading : WizardSuggestionUiState()
    object None : WizardSuggestionUiState()
    data class Error(val displayObject: ErrorDisplayObject) : WizardSuggestionUiState()
}