package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.profile.module.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: ProfileRepository
) : FlowUseCase<Unit, UserSettings>() {
    override fun operation(params: Unit): Flow<UserSettings> =
        repository.getUserSetting()
}