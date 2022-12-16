package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.profile.module.UserProfile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import javax.inject.Inject

class SetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : UseCase<SetUserProfileUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        repository.setUserProfile(params.userProfile)
    }

    data class Params(val userProfile: UserProfile)
}