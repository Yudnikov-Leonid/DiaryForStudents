package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.analytics.domain.AnalyticsDomain
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PerformanceInteractor : SaveAndRestore {
    suspend fun loadData()

    fun actualData(): List<PerformanceDomain>
    fun finalData(): List<PerformanceDomain>

    suspend fun analytics(
        quarter: Int,
        lessonName: String,
        interval: Int,
        showFinal: Boolean
    ): List<AnalyticsDomain>

    fun currentProgressType(): ProgressType
    fun showType(): Boolean
    fun currentQuarter(): Int
    suspend fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson
    suspend fun changeQuarter(quarter: Int)

    fun dataIsLoading(callback: () -> Unit): Boolean

    fun newMarksCount(): Int

    class Base @Inject constructor(
        private val repository: PerformanceRepository,
        private val failureHandler: FailureHandler,
        private val diaryRepository: DiaryRepository,
        private val manageResource: ManageResource
    ) : PerformanceInteractor {
        private var finalLoadCallbackList: MutableList<(() -> Unit)> = ArrayList()
        private var dataIsLoading = false

        override suspend fun loadData() {
            dataIsLoading = true
            repository.loadData()
            dataIsLoading = false
            if (finalLoadCallbackList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    finalLoadCallbackList.forEach {
                        it.invoke()
                    }
                }
                finalLoadCallbackList.clear()
            }
        }

        override fun actualData(): List<PerformanceDomain> {
            return try {
                repository.cachedData()
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message(manageResource)))
            }
        }

        override fun finalData() =
            try {
                repository.cachedFinalData()
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message(manageResource)))
            }

        override suspend fun analytics(
            quarter: Int,
            lessonName: String,
            interval: Int,
            showFinal: Boolean
        ) = try {
            repository.analytics(quarter, lessonName, interval, showFinal)
        } catch (e: Exception) {
            listOf(AnalyticsDomain.Error(failureHandler.handle(e).message(manageResource)))
        }

        override fun currentProgressType() = repository.currentProgressType()

        override fun showType() = repository.showType()

        override fun currentQuarter() = repository.currentQuarter()

        override suspend fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson =
            try {
                diaryRepository.getLesson(lessonName, date)
            } catch (e: Exception) {
                DiaryDomain.Lesson(
                    failureHandler.handle(e).message(manageResource),
                    -1,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            }

        override suspend fun changeQuarter(quarter: Int) {
            repository.changeQuarter(quarter)
        }

        override fun dataIsLoading(callback: () -> Unit): Boolean {
            return if (dataIsLoading) {
                finalLoadCallbackList.add(callback)
                true
            } else
                false
        }

        override fun newMarksCount() = repository.newMarksCount()

        override fun save(bundleWrapper: BundleWrapper.Save) {
            repository.save(bundleWrapper)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            repository.restore(bundleWrapper)
        }
    }
}