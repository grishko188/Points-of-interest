package com.grishko188.domain.features.profile.interactor

import com.grishko188.domain.core.UseCase
import com.grishko188.domain.features.profile.model.ManualSettings
import com.grishko188.domain.features.profile.repo.ProfileRepository
import javax.inject.Inject

class SetUserSettingStateUseCase @Inject constructor(
    private val repository: ProfileRepository
) : UseCase<SetUserSettingStateUseCase.Params, Unit>() {

    override suspend fun operation(params: Params) {
        when (params.setting) {
            ManualSettings.UseSystemTheme -> repository.setUseSystemTheme(params.state)
            ManualSettings.UseDarkTheme -> repository.setUseDarkTheme(params.state)
            ManualSettings.UseAutoGc -> repository.setUseAutoGc(params.state)
        }
    }

    data class Params(
        val setting: ManualSettings,
        val state: Boolean
    )
}