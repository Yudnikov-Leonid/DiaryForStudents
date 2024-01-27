package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository
import com.maxim.diaryforstudents.performance.eduData.FailureHandler

interface PerformanceInteractor {
    suspend fun init()
    fun data(search: String): List<PerformanceDomain>
    fun finalData(search: String): List<PerformanceDomain>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int

    class Base(
        private val repository: EduPerformanceRepository,
        private val failureHandler: FailureHandler
    ) : PerformanceInteractor {
        override suspend fun init() {
            repository.init()
        }

        override fun data(search: String): List<PerformanceDomain> {
            return try {
                repository.cachedData(search).map { it.toDomain() }
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message()))
            }
        }

        override fun finalData(search: String): List<PerformanceDomain> {
            return try {
                repository.cachedFinalData(search).map { it.toDomain() }
            } catch (e: Exception) {
                listOf(PerformanceDomain.Error(failureHandler.handle(e).message()))
            }
        }

        override suspend fun changeQuarter(quarter: Int) = repository.changeQuarter(quarter)
        override fun actualQuarter() = repository.actualQuarter()
    }
}