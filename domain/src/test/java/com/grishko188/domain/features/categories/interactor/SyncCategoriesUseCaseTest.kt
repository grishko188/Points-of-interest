package com.grishko188.domain.features.categories.interactor

import com.grishko188.domain.MockitoHelper
import com.grishko188.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SyncCategoriesUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: SyncCategoriesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = SyncCategoriesUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SyncCategoriesUseCase invokes sync repo function`() = runTest {
        SUT.invoke(Unit)
        Mockito.verify(repository, Mockito.times(1)).sync()
    }

    @Test(expected = Throwable::class)
    fun `test SyncCategoriesUseCase throws exception when sync throws exception`() = runTest {
        MockitoHelper.whenever(repository.sync()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}