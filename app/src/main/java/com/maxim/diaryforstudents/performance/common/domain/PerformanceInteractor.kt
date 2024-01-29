package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType

interface PerformanceInteractor {
    suspend fun init()
    fun data(search: String): List<PerformanceDomain>
    fun finalData(search: String): List<PerformanceDomain>
    suspend fun changeQuarter(quarter: Int)
    fun progressType(): ProgressType

    fun actualQuarter(): Int

    class Base(
        private val repository: PerformanceRepository,
        private val simpleStorage: SimpleStorage,
        private val failureHandler: FailureHandler,
        private val mapper: PerformanceData.Mapper<PerformanceDomain>
    ) : PerformanceInteractor {
        override suspend fun init() {
            repository.init()
        }

        override fun data(search: String): List<PerformanceDomain> {
            return try {
                repository.cachedData(search).map { it.map(mapper) }
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

        override fun actualQuarter() = repository.actualQuarter()

        companion object {
            private const val SHOW_PROGRESS_KEY = "actual_show_progress"
            private const val PROGRESS_COMPARED_KEY = "actual_progress_compared"
            private const val SORT_BY_KEY = "actual_sort_by"
        }
    }
}