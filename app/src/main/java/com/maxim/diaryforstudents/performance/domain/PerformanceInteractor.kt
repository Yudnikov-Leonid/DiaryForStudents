package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.data.FailureHandler
import com.maxim.diaryforstudents.performance.data.PerformanceData

interface PerformanceInteractor {
    suspend fun init()
    fun data(search: String): List<PerformanceDomain>
    fun finalData(search: String): List<PerformanceDomain>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int

    class Base(
        private val repository: PerformanceRepository,
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
        override fun actualQuarter() = repository.actualQuarter()
    }
}