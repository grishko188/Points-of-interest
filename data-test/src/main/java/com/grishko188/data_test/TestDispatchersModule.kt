package com.grishko188.data_test

import com.grishko188.domain.di.DispatchersModule
import com.grishko188.domain.di.UseCaseDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
object TestDispatchersModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    @UseCaseDispatcher
    fun provideDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}