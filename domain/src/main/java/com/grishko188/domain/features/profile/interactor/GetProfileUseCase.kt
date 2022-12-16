package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.FlowUseCase
import com.grishko188.domain.features.profile.model.Profile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : FlowUseCase<Unit, Profile>() {

    override fun operation(params: Unit): Flow<Profile> =
        repository.getUserProfile()
            .combine(repository.getUserSetting()) { profile, settings ->
                Profile(profile, settings)
            }
}