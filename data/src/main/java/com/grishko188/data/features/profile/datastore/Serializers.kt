package com.grishko188.data.features.profile.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserProfileSerializer @Inject constructor() : Serializer<UserProfileProto> {
    override val defaultValue: UserProfileProto = UserProfileProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProfileProto =
        try {
            // readFrom is already called on the data store background thread
            @Suppress("BlockingMethodInNonBlockingContext")
            UserProfileProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserProfileProto, output: OutputStream) {
        // writeTo is already called on the data store background thread
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
    }
}

class UserSettingsSerializer @Inject constructor() : Serializer<UserSettingsProto> {
    override val defaultValue: UserSettingsProto = UserSettingsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettingsProto =
        try {
            // readFrom is already called on the data store background thread
            @Suppress("BlockingMethodInNonBlockingContext")
            UserSettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserSettingsProto, output: OutputStream) {
        // writeTo is already called on the data store background thread
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
    }
}

