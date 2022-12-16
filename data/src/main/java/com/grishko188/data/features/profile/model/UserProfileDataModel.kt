package com.grishko188.data.features.profile.model

import com.grishko188.data.features.profile.datastore.UserProfileProto
import com.grishko188.domain.features.profile.model.UserProfile

data class UserProfileDataModel(
    val token: String?,
    val name: String?,
    val email: String?,
    val profileImage: String?
)

fun UserProfileProto.toDataModel() = UserProfileDataModel(
    token = authToken,
    name = name,
    email = email,
    profileImage = profileImage
)

fun UserProfileDataModel.toDomain() = UserProfile(
    authToken = token,
    name = name,
    email = email,
    image = profileImage
)

fun UserProfile.toDataModel() = UserProfileDataModel(
    token = authToken,
    name = name,
    email = email,
    profileImage = image
)