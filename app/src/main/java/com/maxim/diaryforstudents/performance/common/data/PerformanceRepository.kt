package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsData
import java.util.Calendar

interface PerformanceRepository {
    suspend fun initActual()
    suspend fun initFinal()
    fun cachedData(search: String): List<PerformanceData>
    fun cachedFinalData(search: String): List<PerformanceData>
    suspend fun changeQuarter(quarter: Int)

    suspend fun analytics(
        quarter: Int,
        lessonName: String,
        interval: Int,
        showFinal: Boolean
    ): List<AnalyticsData>

    fun actualQuarter(): Int

    class Base(private val cloudDataSource: PerformanceCloudDataSource) :
        PerformanceRepository {
        private var dataException: Exception? = null
        private var finalDataException: Exception? = null

        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()

        override suspend fun initActual() {
            dataException = null
            cache.clear()

            try {
                cache.addAll(cloudDataSource.data(actualQuarter(), true))
            } catch (e: Exception) {
                dataException = e
            }
        }

        override suspend fun initFinal() {
            finalDataException = null
            finalCache.clear()

            try {
                finalCache.addAll(cloudDataSource.finalData())
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

        override suspend fun analytics(
            quarter: Int,
            lessonName: String,
            interval: Int,
            showFinal: Boolean
        ): List<AnalyticsData> {
            val list = ArrayList(cloudDataSource.analytics(quarter, lessonName, interval))
            if (showFinal)
                list.add(cloudDataSource.finalAnalytics(quarter))
            return list
        }

        //todo refactor
        override fun actualQuarter() = when (Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            in 0..91 -> 3
            in 92..242 -> 4
            in 243..305 -> 1
            else -> 2
        }
    }
}