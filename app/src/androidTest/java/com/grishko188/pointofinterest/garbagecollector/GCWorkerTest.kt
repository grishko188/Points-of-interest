package com.grishko188.pointofinterest.garbagecollector

import android.graphics.Color
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.ListenableWorker.Result.Success
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import com.grishko188.data.core.Local
import com.grishko188.data.features.categories.datasource.CategoriesDataSource
import com.grishko188.data.features.categories.model.CategoryDataModel
import com.grishko188.data.features.poi.datasource.PoiDataSource
import com.grishko188.data.features.poi.model.PoiDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

@HiltAndroidTest
class GCWorkerTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    @Local
    lateinit var poiDataSource: PoiDataSource

    @Inject
    @Local
    lateinit var categoriesDataSource: CategoriesDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun test_GCWorker_periodic_task_is_ENQUEUED() {

        val request = PeriodicWorkRequestBuilder<GCWorker>(Duration.ofDays(1))
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .addTag(GCWorker.TAG_GC)
            .build()

        val workManager = WorkManager.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(InstrumentationRegistry.getInstrumentation().targetContext)

        workManager.enqueue(request).result.get()
        testDriver?.setPeriodDelayMet(request.id)
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.ENQUEUED, workInfo.state)
    }

    @Test
    fun test_GCWorker_periodic_task_is_ENQUEUED_and_after_cancel_by_tag_is_CANCELED() {

        val request = PeriodicWorkRequestBuilder<GCWorker>(Duration.ofDays(1))
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .addTag(GCWorker.TAG_GC)
            .build()

        val workManager = WorkManager.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(InstrumentationRegistry.getInstrumentation().targetContext)

        workManager.enqueue(request).result.get()
        testDriver?.setPeriodDelayMet(request.id)
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.ENQUEUED, workInfo.state)

        workManager.cancelAllWorkByTag(GCWorker.TAG_GC)

        val canceledWorkInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.CANCELLED, canceledWorkInfo.state)
    }

    @Test
    fun test_GCWorker_dry_run_result() {
        val worker = TestListenableWorkerBuilder<GCWorker>(InstrumentationRegistry.getInstrumentation().targetContext)
            .setWorkerFactory(workerFactory)
            .build()

        val result = worker.startWork().get()
        assertTrue(result is Success)
        assertEquals(0, result.outputData.getInt(GCWorker.KEY_DELETED_ITEMS_COUNT, -1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_GCWorker_run_to_delete_two_items() = runTest {
        val testCategories = arrayListOf(
            CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(2, "Medium", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(3, "Normal", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(4, "Low", Color.RED, type = "SEVERITY", false),
            CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true),
            CategoryDataModel(6, "Name 2", Color.WHITE, type = "PERSONAL", true),
            CategoryDataModel(7, "Name 3", Color.WHITE, type = "PERSONAL", true)
        )

        categoriesDataSource.addCategories(testCategories)

        val testCreationPoiList = arrayListOf(
            PoiDataModel(
                id = 1,
                title = "A Title",
                body = "A Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 2.days,
                commentsCount = 0,
                severity = 0,
                categories = listOf(
                    CategoryDataModel(1, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(5, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            ),
            PoiDataModel(
                id = 2,
                title = "B Title",
                body = "B Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 180.days,
                commentsCount = 0,
                severity = 1,
                categories = listOf(
                    CategoryDataModel(2, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(6, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            ),
            PoiDataModel(
                id = 3,
                title = "C Title",
                body = "C Body",
                imageUrl = "https://www.google.com/image",
                contentLink = "https://www.google.com/somethingelse?query=1",
                creationDate = Clock.System.now() - 95.days,
                commentsCount = 0,
                severity = 1,
                categories = listOf(
                    CategoryDataModel(2, "High", Color.RED, type = "SEVERITY", false),
                    CategoryDataModel(6, "Name", Color.WHITE, type = "PERSONAL", true)
                )
            )
        )

        testCreationPoiList.forEach {
            poiDataSource.insertPoi(it)
        }

        val worker = TestListenableWorkerBuilder<GCWorker>(InstrumentationRegistry.getInstrumentation().targetContext)
            .setWorkerFactory(workerFactory)
            .build()

        val result = withContext(Dispatchers.IO) {
            worker.startWork().get()
        }
        assertTrue(result is Success)
        assertEquals(2, result.outputData.getInt(GCWorker.KEY_DELETED_ITEMS_COUNT, -1))
    }
}