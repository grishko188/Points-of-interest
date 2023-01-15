package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.profile.model.Profile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Profile>(dispatcher) {

    override fun operation(params: Unit): Flow<Profile> =
        repository.getUserProfile()
            .combine(repository.getUserSetting()) { profile, settings ->
                Profile(profile, settings)
            }
}