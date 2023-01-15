package com.grishko188.pointofinterest.features.profile.vm

import android.net.Uri
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.grishko188.domain.features.profile.interactor.DeleteUserProfileUseCase
import com.grishko188.domain.features.profile.interactor.GetProfileUseCase
import com.grishko188.domain.features.profile.interactor.SetUserProfileUseCase
import com.grishko188.domain.features.profile.interactor.SetUserSettingStateUseCase
import com.grishko188.domain.features.profile.repo.ProfileRepository
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
    lateinit var repository: ProfileRepository

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
        assertNull((state.sections.find { it.type == ProfileSectionType.ACCOUNT } as? ProfileSectionItem.AccountSectionItem)?.userInfo?.fullName)

        SUT.onUserSignedIn(mockTask)

        runCurrent()
        val updated = SUT.profileState.value
        assertIs<ProfileScreenUiState.Result>(updated)

        assertEquals("Test Name", (updated.sections.find { it.type == ProfileSectionType.ACCOUNT } as? ProfileSectionItem.AccountSectionItem)?.userInfo?.fullName)
        collectJob.cancel()
    }
}