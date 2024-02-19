package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PerformanceInteractor: SaveAndRestore {
    suspend fun loadActualData()
    suspend fun loadFinalData()

    fun actualData(): List<PerformanceDomain>
    fun finalData(): List<PerformanceDomain>

    fun currentProgressType(): ProgressType
    fun currentQuarter(): Int
    suspend fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson
    suspend fun changeQuarter(quarter: Int)

    fun finalDataIsEmpty(reload: Reload): Boolean

    class Base(
        private val repository: PerformanceRepository,
        private val simpleStorage: SimpleStorage,
        private val failureHandler: FailureHandler,
        private val mapper: PerformanceData.Mapper<PerformanceDomain>,
        private val diaryRepository: DiaryRepository,
        private val diaryMapper: DiaryData.Mapper<DiaryDomain>
    ) : PerformanceInteractor {
        private var finalLoadCallback: Reload? = null

        override suspend fun loadActualData() {
            repository.loadActualData()
        }

        override suspend fun loadFinalData() {
            repository.initFinalData()
            finalLoadCallback?.let {
                withContext(Dispatchers.Main) {
                    it.reload()
                }
                finalLoadCallback = null
            }
        }

        override fun actualData(): List<PerformanceDomain> {
            val sortBy = simpleStorage.read(ActualSettingsViewModel.SORT_BY_KEY, 0)
            val sortingOrder = simpleStorage.read(ActualSettingsViewModel.SORTING_ORDER_KEY, 0)
            return try {
                val data = repository.cachedData().sortedBy {
                    when (sortBy) {
                        0 -> 0f
                        1 -> it.average()
                        2 -> currentProgressType().selectProgress(
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

        override fun finalData() =
            try {
                repository.cachedFinalData().map { it.map(mapper) }
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message()))
            }

        override fun currentProgressType() =
            if (!simpleStorage.read(
                    ActualSettingsViewModel.SHOW_PROGRESS_KEY,
                    true
                )
            ) ProgressType.Hide
            else when (simpleStorage.read(ActualSettingsViewModel.PROGRESS_COMPARED_KEY, 0)) {
                0 -> ProgressType.AWeekAgo
                1 -> ProgressType.TwoWeeksAgo
                2 -> ProgressType.AMonthAgo
                else -> ProgressType.PreviousQuarter
            }

        override fun currentQuarter() = repository.currentQuarter()

        override suspend fun getLessonByMark(lessonName: String, date: String) =
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

        override suspend fun changeQuarter(quarter: Int) {
            repository.changeQuarter(quarter)
        }

        override fun finalDataIsEmpty(reload: Reload): Boolean {
            val isEmpty =
                if (finalData().isEmpty()) true
                else finalData().first() is PerformanceDomain.Error || finalData().first() is PerformanceDomain.Empty
            if (isEmpty)
                finalLoadCallback = reload
            return isEmpty
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            repository.save(bundleWrapper)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            repository.restore(bundleWrapper)
        }
    }
}