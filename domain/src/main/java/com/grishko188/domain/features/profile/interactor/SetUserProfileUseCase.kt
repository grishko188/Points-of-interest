package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.profile.model.UserProfile
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<SetUserProfileUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.setUserProfile(params.userProfile)
    }

    data class Params(val userProfile: UserProfile)
}