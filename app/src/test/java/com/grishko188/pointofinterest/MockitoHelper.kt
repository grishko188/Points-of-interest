package com.grishko188.pointofinterest

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

object MockitoHelper {

    /**
     * Use this in place of captor.capture() if you are trying to capture an argument that is not nullable.
     */
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    /**
     * Use this in place of ArgumentMatchers.any() if you are not passing/expecting a specific value as a parameter.
     */
    fun <T> any(type: Class<T>): T = Mockito.any(type)

    /**
     * Use this to match any not null argument.
     */
    inline fun <reified T> anyNotNull(): T = Mockito.any(T::class.java)

    /**
     * Matches any object, excluding nulls.
     */
    inline fun <reified T> anyNonNull(): T = Mockito.any<T>(T::class.java)

    /**
     * To avoid having to use the backtick key for @see[org.mockito.Mockito.when].
     */
    fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

    /**
     * To avoid having to use the @see[org.mockito.Mock] annotation.
     */
    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    /**
     * Less verbose way of creating an @see[org.mockito.ArgumentCaptor].
     */
    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)

    fun <T : Any> nonNullEq(value: T): T = eq(value) ?: value
}