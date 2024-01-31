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

    class Base(
        private val cloudDataSource: PerformanceCloudDataSource,
        private val handleResponse: HandleResponse
    ) : PerformanceRepository {
        private var dataException: Exception? = null
        private var finalDataException: Exception? = null

        private val responseCache = mutableMapOf<Int, List<CloudLesson>>()
        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()
        private val finalResponseCache = mutableListOf<PerformanceFinalLesson>()

        override suspend fun initActual() {
            dataException = null
            cache.clear()

            try {
                val dates = dates(actualQuarter())
                responseCache[actualQuarter()] = cloudDataSource.data(dates.first, dates.second)
                cache.addAll(
                    handleResponse.marks(responseCache[actualQuarter()]!!, true, actualQuarter())
                )

            } catch (e: Exception) {
                dataException = e
            }
        }

        override suspend fun initFinal() {
            finalDataException = null
            finalCache.clear()

            try {
                finalResponseCache.clear()
                finalResponseCache.addAll(cloudDataSource.finalData())
                finalCache.addAll(handleResponse.finalMarks(finalResponseCache))
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
                val dates = dates(quarter)
                cache.addAll(
                    handleResponse.marks(
                        cloudDataSource.data(dates.first, dates.second),
                        quarter == actualQuarter(), quarter
                    )
                )
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
            val dates = dates(quarter)
            val list = ArrayList(handleResponse.analytics(
                responseCache[quarter] ?: cloudDataSource.data(dates.first, dates.second),
                quarter,
                lessonName,
                dates.first,
                dates.second,
                interval
            ))
            if (showFinal)
                list.add(handleResponse.finalAnalytics(finalResponseCache, quarter, actualQuarter()))
            return list
        }

        //todo refactor
        override fun actualQuarter() = when (Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            in 0..91 -> 3
            in 92..242 -> 4
            in 243..305 -> 1
            else -> 2
        }

        //todo
        private fun dates(quarter: Int): Pair<String, String> =
            when (quarter) {
                1 -> Pair("01.09.2023", "27.10.2023")
                2 -> Pair("06.11.2023", "29.12.2023")
                3 -> Pair("09.01.2024", "22.03.2024")
                4 -> Pair("01.04.2024", "26.05.2024")
                else -> Pair("01.09.2023", "26.05.2024")
            }
    }
}