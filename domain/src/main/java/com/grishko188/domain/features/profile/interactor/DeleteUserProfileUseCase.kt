package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.di.UseCaseDispatcher
import com.grishko188.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun operation(params: Unit) {
        repository.deleteUserProfile()
    }
}