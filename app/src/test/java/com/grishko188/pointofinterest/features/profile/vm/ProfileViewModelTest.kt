package com.grishko188.pointofinterest.features.profile.vm

import android.net.Uri
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.grishko188.domain.features.profile.interactor.DeleteUserProfileUseCase
import com.grishko188.domain.features.profile.interactor.GetProfileUseCase
import com.grishko188.domain.features.profile.interactor.SetUserProfileUseCase
import com.grishko188.domain.features.profile.interactor.SetUserSettingStateUseCase
import com.grishko188.pointofinterest.MockitoHelper.anyNonNull
import com.grishko188.pointofinterest.MockitoHelper.mock
import com.grishko188.pointofinterest.MockitoHelper.whenever
import com.grishko188.pointofinterest.features.profile.models.ProfileScreenUiState
import com.grishko188.pointofinterest.features.profile.models.ProfileSectionItem
import com.grishko188.pointofinterest.features.profile.models.ProfileSectionType
import com.grishko188.pointofinterest.features.profile.models.wipSections
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class ProfileViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mockRule = MockitoJUnit.rule()

    @Mock
    lateinit var workManager: WorkManager

    @Inject
    lateinit var setUserProfileUseCase: SetUserProfileUseCase

    @Inject
    lateinit var setUserSettingStateUseCase: SetUserSettingStateUseCase

    @Inject
    lateinit var deleteUserProfileUseCase: DeleteUserProfileUseCase

    @Inject
    lateinit var getProfileUseCase: GetProfileUseCase

    lateinit var SUT: ProfileViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()

        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = ProfileViewModel(
            getProfileUseCase,
            setUserSettingStateUseCase,
            setUserProfileUseCase,
            deleteUserProfileUseCase,
            workManager
        )
    }

    @After
    fun teardown() {
        Mockito.validateMockitoUsage()
        Dispatchers.resetMain()
    }

    @Test
    fun `test ProfileViewModel initial profileState is emptyList`() = runTest {
        assertIs<ProfileScreenUiState.Empty>(SUT.profileState.value)
    }

    @Test
    fun `test ProfileViewModel profile state is Result once collecting`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(state)
        assertEquals(state.sections.size, ProfileSectionType.values().filter { it !in wipSections }.size)
        collectJob.cancel()
    }

    @Test
    fun `test ProfileViewModel onUserSignedIn updates profileState with updated profile information`() = runTest {
        val mockTask = mock<Task<GoogleSignInAccount>>()
        val mockAccount = mock<GoogleSignInAccount>()
        val mockUri = mock<Uri>()
        whenever(mockTask.result).thenReturn(mockAccount)

        whenever(mockAccount.idToken).thenReturn("Token")
        whenever(mockAccount.displayName).thenReturn("Test Name")
        whenever(mockAccount.email).thenReturn("email@email.com")
        whenever(mockAccount.photoUrl).thenReturn(mockUri)
        whenever(mockUri.toString()).thenReturn("uri")


        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(state)
        assertNull(state.findSection<ProfileSectionItem.AccountSectionItem>(ProfileSectionType.ACCOUNT)?.userInfo?.fullName)

        SUT.onUserSignedIn(mockTask)

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertEquals(
            "Test Name",
            updated.findSection<ProfileSectionItem.AccountSectionItem>(ProfileSectionType.ACCOUNT)?.userInfo?.fullName
        )
        collectJob.cancel()
    }

    @Test
    fun `test ProfileViewModel onSignOutClicked updates profileState with empty profile information`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(state)
        assertNull(state.findSection<ProfileSectionItem.AccountSectionItem>(ProfileSectionType.ACCOUNT)?.userInfo?.fullName)

        SUT.onSignOutClicked()

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertNull(updated.findSection<ProfileSectionItem.AccountSectionItem>(ProfileSectionType.ACCOUNT)?.userInfo?.fullName)
        collectJob.cancel()
    }

    @Test
    fun `test ProfileViewModel onSettingsToggled SYSTEM_THEME updates profileState with updated user settings data`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value

        assertIs<ProfileScreenUiState.Result>(state)
        assertEquals(true, state.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.SYSTEM_THEME)?.state)
        assertEquals(false, state.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.DARK_THEME)?.isEnabled)

        SUT.onSettingsToggled(ProfileSectionType.SYSTEM_THEME, currentState = true)

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertEquals(false, updated.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.SYSTEM_THEME)?.state)
        assertEquals(true, updated.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.DARK_THEME)?.isEnabled)
        collectJob.cancel()
    }

    @Test
    fun `test ProfileViewModel onSettingsToggled DARK_THEME updates profileState with updated user settings data`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value

        assertIs<ProfileScreenUiState.Result>(state)
        assertEquals(false, state.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.DARK_THEME)?.state)

        SUT.onSettingsToggled(ProfileSectionType.DARK_THEME, currentState = false)

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertEquals(true, updated.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.DARK_THEME)?.state)
        collectJob.cancel()
    }

    @Test
    fun `test ProfileViewModel onSettingsToggled GARBAGE_COLLECTOR updates profileState with updated user settings and triggers work manager`() = runTest {
        val resultOperationMock = mock<Operation>()
        whenever(workManager.enqueue(anyNonNull<WorkRequest>())).thenReturn(resultOperationMock)
        whenever(workManager.cancelAllWorkByTag(anyNonNull())).thenReturn(resultOperationMock)

        val collectJob = launch(UnconfinedTestDispatcher()) { SUT.profileState.collect() }
        val state = SUT.profileState.value

        assertIs<ProfileScreenUiState.Result>(state)
        assertEquals(false, state.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.GARBAGE_COLLECTOR)?.state)

        SUT.onSettingsToggled(ProfileSectionType.GARBAGE_COLLECTOR, currentState = false)

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertEquals(true, updated.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.GARBAGE_COLLECTOR)?.state)
        verify(workManager, times(1)).enqueue(anyNonNull<WorkRequest>())

        SUT.onSettingsToggled(ProfileSectionType.GARBAGE_COLLECTOR, currentState = true)

        val updated2 = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated2)
        assertEquals(false, updated2.findSection<ProfileSectionItem.BooleanSettingsItem>(ProfileSectionType.GARBAGE_COLLECTOR)?.state)
        verify(workManager, times(1)).cancelAllWorkByTag(anyNonNull())

        collectJob.cancel()
    }

    private inline fun <reified T> ProfileScreenUiState.Result.findSection(type: ProfileSectionType) =
        this.sections.find { it.type == type } as? T
}