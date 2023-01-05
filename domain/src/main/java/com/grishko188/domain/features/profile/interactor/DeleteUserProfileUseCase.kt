package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.profile.repo.ProfileRepository
import javax.inject.Inject

class DeleteUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : UseCase<Unit, Unit>() {
    override suspend fun operation(params: Unit) {
        repository.deleteUserProfile()
    }
}