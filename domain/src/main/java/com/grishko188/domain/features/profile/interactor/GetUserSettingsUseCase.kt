package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.profile.model.UserSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, UserSettings>(dispatcher) {
    override fun operation(params: Unit): Flow<UserSettings> =
        repository.getUserSetting()
}