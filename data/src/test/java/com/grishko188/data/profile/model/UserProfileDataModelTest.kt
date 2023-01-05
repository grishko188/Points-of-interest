package com.grishko188.data.profile.model

import com.grishko188.data.features.profile.datastore.UserProfileProto
import com.grishko188.data.features.profile.model.UserProfileDataModel
import com.grishko188.data.features.profile.model.toDataModel
import com.grishko188.data.features.profile.model.toDomain
import com.grishko188.domain.features.profile.model.UserProfile
import org.junit.Test
import kotlin.test.assertEquals

class UserProfileDataModelTest {

    @Test
    fun `test UserProfileDataModel_toDomain() function returns UserProfile model with correct fields`() {
        val dataModel = UserProfileDataModel(
            token = "Token",
            name = "Name",
            email = "email",
            profileImage = "image"
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.token, domainModel.authToken)
        assertEquals(dataModel.name, domainModel.name)
        assertEquals(dataModel.email, domainModel.email)
        assertEquals(dataModel.profileImage, domainModel.image)
    }

    @Test
    fun `test UserProfile_toDataModel() function returns UserProfileDataModel model with correct fields`() {
        val domainModel = UserProfile(
            authToken = "Token",
            name = "Name",
            email = "email",
            image = "image"
        )

        val dataModel = domainModel.toDataModel()

        assertEquals(domainModel.authToken, dataModel.token)
        assertEquals(domainModel.name, dataModel.name)
        assertEquals(domainModel.email, dataModel.email)
        assertEquals(domainModel.image, dataModel.profileImage)
    }

    @Test
    fun `test UserProfileProto_toDataModel() function returns CategoryDataModel model with correct fields`() {
        val userProfileProto = UserProfileProto.newBuilder().apply {
            authToken = "Token"
            name = "Name"
            email = "Email"
            profileImage = "Image"
        }.build()

        val dataModel = userProfileProto.toDataModel()

        assertEquals(dataModel.token, userProfileProto.authToken)
        assertEquals(dataModel.name, userProfileProto.name)
        assertEquals(dataModel.email, userProfileProto.email)
        assertEquals(dataModel.profileImage,userProfileProto.profileImage)
    }
}