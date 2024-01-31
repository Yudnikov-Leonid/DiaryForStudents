package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel.Companion.PROGRESS_COMPARED_KEY
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel.Companion.SHOW_PROGRESS_KEY
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel.Companion.SORTING_ORDER_KEY
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel.Companion.SORT_BY_KEY
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.analytics.domain.AnalyticsDomain
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType

interface PerformanceInteractor {
    suspend fun initActual()
    suspend fun initFinal()
    fun data(search: String): List<PerformanceDomain>
    fun finalData(search: String): List<PerformanceDomain>
    suspend fun changeQuarter(quarter: Int)
    fun progressType(): ProgressType

    fun finalDataIsEmpty(): Boolean

    suspend fun analytics(quarter: Int, lessonName: String, interval: Int, showFinal: Boolean): List<AnalyticsDomain>

    suspend fun getLesson(lessonName: String, date: String): DiaryDomain.Lesson

    fun actualQuarter(): Int

    fun save(bundleWrapper: BundleWrapper.Save)
    fun restore(bundleWrapper: BundleWrapper.Restore)

    class Base(
        private val repository: PerformanceRepository,
        private val diaryRepository: DiaryRepository,
        private val simpleStorage: SimpleStorage,
        private val failureHandler: FailureHandler,
        private val mapper: PerformanceData.Mapper<PerformanceDomain>,
        private val diaryMapper: DiaryData.Mapper<DiaryDomain>
    ) : PerformanceInteractor {
        override suspend fun initActual() {
            repository.initActual()
        }

        override suspend fun initFinal() {
            repository.initFinal()
        }

        override fun data(search: String): List<PerformanceDomain> {
            val sortBy = simpleStorage.read(SORT_BY_KEY, 0)
            val sortingOrder = simpleStorage.read(SORTING_ORDER_KEY, 0)
            return try {
                val data = repository.cachedData(search).sortedBy {
                    when (sortBy) {
                        0 -> 0f
                        1 -> it.average()
                        2 -> progressType().selectProgress(
                            it.progress()[0],
                            it.progress()[1],
                            it.progress()[2],
                            it.progress()[3]
                        ).toFloat()

                        else -> it.marksCount().toFloat()
                    }
                }.map { it.map(mapper) }
                if (sortingOrder == 0) data else data.reversed()
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message()))
            }
        }

        override fun finalData(search: String): List<PerformanceDomain> {
            return try {
                repository.cachedFinalData(search).map { it.map(mapper) }
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message()))
            }
        }

        override suspend fun changeQuarter(quarter: Int) = repository.changeQuarter(quarter)

        //todo constants in actualSettingsViewModel
        override fun progressType(): ProgressType {
            return if (!simpleStorage.read(SHOW_PROGRESS_KEY, true)) ProgressType.Hide
            else when (simpleStorage.read(PROGRESS_COMPARED_KEY, 0)) {
                0 -> ProgressType.AWeekAgo
                1 -> ProgressType.TwoWeeksAgo
                2 -> ProgressType.AMonthAgo
                else -> ProgressType.PreviousQuarter
            }
        }

        override fun finalDataIsEmpty() =
            if (finalData("").isEmpty()) true
            else finalData("").first() is PerformanceDomain.Error || finalData("").first() is PerformanceDomain.Empty

        override suspend fun analytics(
            quarter: Int,
            lessonName: String,
            interval: Int,
            showFinal: Boolean
        ): List<AnalyticsDomain> = try {
            repository.analytics(quarter, lessonName, interval, showFinal).map { it.toDomain() }
        } catch (e: Exception) {
            listOf(AnalyticsDomain.Error(failureHandler.handle(e).message()))
        }


        override suspend fun getLesson(lessonName: String, date: String): DiaryDomain.Lesson =
            try {
                diaryRepository.getLesson(lessonName, date).map(diaryMapper) as DiaryDomain.Lesson
            } catch (e: Exception) {
                DiaryDomain.Lesson(
                    failureHandler.handle(e).message(),
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


        override fun actualQuarter() = repository.actualQuarter()
        override fun save(bundleWrapper: BundleWrapper.Save) {
            repository.save("", bundleWrapper)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            repository.restore("", bundleWrapper)
        }
    }
}