package com.grishko188.domain.features.poi.interactor

import com.grishko188.domain.MockitoHelper.anyNonNull
import com.grishko188.domain.MockitoHelper.argumentCaptor
import com.grishko188.domain.MockitoHelper.capture
import com.grishko188.domain.MockitoHelper.mock
import com.grishko188.domain.MockitoHelper.whenever
import com.grishko188.domain.features.poi.models.WizardSuggestion
import com.grishko188.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetWizardSuggestionUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetWizardSuggestionUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetWizardSuggestionUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetWizardSuggestionUseCase invokes getWizardSuggestion repo function`() = runTest {
        val contentUrl = "https://something.com"
        val suggestion = mock<WizardSuggestion>()
        whenever(repository.getWizardSuggestion(anyNonNull())).thenReturn(suggestion)
        val result = SUT.invoke(GetWizardSuggestionUseCase.Params(contentUrl))
        val captor = argumentCaptor<String>()
        verify(repository, Mockito.times(1)).getWizardSuggestion(capture(captor))
        assertEquals(contentUrl, captor.value)
        assertEquals(suggestion, result)
    }

    @Test(expected = Throwable::class)
    fun `test GetWizardSuggestionUseCase throws exception when getWizardSuggestion throws exception`() = runTest {
        val contentUrl = "https://something.com"
        whenever(repository.getWizardSuggestion(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(GetWizardSuggestionUseCase.Params(contentUrl))
    }
}