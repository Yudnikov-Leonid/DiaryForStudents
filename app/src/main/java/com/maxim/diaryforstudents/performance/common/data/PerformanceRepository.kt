package com.maxim.diaryforstudents.performance.common.data

import java.util.Calendar

interface PerformanceRepository {
    suspend fun init()
    fun cachedData(search: String): List<PerformanceData>
    fun cachedFinalData(search: String): List<PerformanceData>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int

    class Base(private val cloudDataSource: PerformanceCloudDataSource) :
        PerformanceRepository {
        private var dataException: Exception? = null
        private var finalDataException: Exception? = null

        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()

        override suspend fun init() {
            dataException = null
            finalDataException = null
            cache.clear()
            finalCache.clear()

            try {
                finalCache.addAll(cloudDataSource.finalData())
            } catch (e: Exception) {
                dataException = e
            }


            try {
                cache.addAll(cloudDataSource.data(actualQuarter(), true))
            } catch (e: Exception) {
                finalDataException = e
            }
        }

        override fun cachedData(search: String) =
            dataException?.let {
                throw it
            } ?: cache.filter { it.search(search) }
                .ifEmpty { listOf(PerformanceData.Empty) }

        override fun cachedFinalData(search: String) =
            finalDataException?.let { throw it } ?: finalCache.filter { it.search(search) }
                .ifEmpty { listOf(PerformanceData.Empty) }

        override suspend fun changeQuarter(quarter: Int) {
            dataException = null
            cache.clear()
            try {
                cache.addAll(cloudDataSource.data(quarter, quarter == actualQuarter()))
            } catch (e: Exception) {
                dataException = e
            }
        }

        override fun actualQuarter() = when (Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            in 0..91 -> 3
            in 92..242 -> 4
            in 243..305 -> 1
            else -> 2
        }
    }
}