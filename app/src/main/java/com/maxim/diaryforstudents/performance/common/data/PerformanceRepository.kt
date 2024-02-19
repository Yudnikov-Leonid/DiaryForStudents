package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.analytics.data.AnalyticsData
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import java.io.Serializable
import java.util.Calendar

interface PerformanceRepository: SaveAndRestore {
    suspend fun loadActualData()
    suspend fun initFinalData()
    fun cachedData(): List<PerformanceData>
    fun cachedFinalData(): List<PerformanceData>
    suspend fun changeQuarter(quarter: Int)

    suspend fun analytics(
        quarter: Int,
        lessonName: String,
        interval: Int,
        showFinal: Boolean
    ): List<AnalyticsData>

    fun currentQuarter(): Int

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

        private val periods = mutableListOf<Pair<String, String>>()
        private var actualQuarter = 1

        override suspend fun loadActualData() {
            dataException = null
            cache.clear()

            try {
                if (periods.isEmpty()) {
                    periods.addAll(
                        cloudDataSource.periods().map { Pair(it.DATE_BEGIN, it.DATE_END) })
                    val calendar = Calendar.getInstance()
                    for (i in periods.indices) {
                        var split = periods[i].first.split('.')
                        calendar.apply {
                            set(Calendar.DAY_OF_MONTH, split[0].toInt())
                            set(Calendar.MONTH, split[1].toInt() - 1)
                            set(Calendar.YEAR, split[2].toInt())
                        }
                        if (calendar.timeInMillis / 86400000 > System.currentTimeMillis() / 86400000)
                            continue
                        split = periods[i].second.split('.')
                        calendar.apply {
                            set(Calendar.DAY_OF_MONTH, split[0].toInt())
                            set(Calendar.MONTH, split[1].toInt() - 1)
                            set(Calendar.YEAR, split[2].toInt())
                        }
                        if (calendar.timeInMillis / 86400000 < System.currentTimeMillis() / 86400000)
                            continue
                        actualQuarter = i + 1
                        break
                    }
                    periods.add(Pair(periods.first().first, periods.last().second))
                }
                val dates = periods[currentQuarter() - 1]
                responseCache[currentQuarter()] = cloudDataSource.data(dates.first, dates.second)
                cache.addAll(
                    handleResponse.lessons(responseCache[currentQuarter()]!!, true, currentQuarter())
                )

            } catch (e: Exception) {
                dataException = e
            }
        }

        override suspend fun initFinalData() {
            finalDataException = null
            finalCache.clear()

            try {
                finalResponseCache.clear()
                finalResponseCache.addAll(cloudDataSource.finalData())
                finalCache.addAll(handleResponse.finalMarksLessons(finalResponseCache))
            } catch (e: Exception) {
                finalDataException = e
            }
        }

        override fun cachedData() =
            dataException?.let {
                throw it
            } ?: cache.ifEmpty { listOf(PerformanceData.Empty) }

        override fun cachedFinalData() =
            finalDataException?.let { throw it } ?: finalCache.ifEmpty { listOf(PerformanceData.Empty) }

        override suspend fun changeQuarter(quarter: Int) {
            dataException = null
            cache.clear()
            try {
                val dates = periods[quarter - 1]
                cache.addAll(
                    handleResponse.lessons(
                        cloudDataSource.data(dates.first, dates.second),
                        quarter == currentQuarter(), quarter
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
            val dates = periods[quarter - 1]
            val list = ArrayList(
                handleResponse.analytics(
                    responseCache[quarter] ?: cloudDataSource.data(dates.first, dates.second),
                    quarter,
                    lessonName,
                    dates.first,
                    dates.second,
                    interval
                )
            )
            if (showFinal)
                list.add(
                    handleResponse.finalAnalytics(
                        finalResponseCache,
                        quarter,
                        currentQuarter()
                    )
                )
            return list
        }

        override fun currentQuarter() = actualQuarter

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(PERIODS_RESTORE_KEY, SerializableList(periods))
            bundleWrapper.save(ACTUAL_QUARTER_RESTORE_KEY, actualQuarter)
            handleResponse.save(bundleWrapper)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            val data = bundleWrapper.restore<SerializableList>(PERIODS_RESTORE_KEY)
            periods.addAll(
                data?.list ?: emptyList()
            )
            actualQuarter = bundleWrapper.restore(ACTUAL_QUARTER_RESTORE_KEY) ?: 1
            handleResponse.restore(bundleWrapper)
        }

        companion object {
            private const val PERIODS_RESTORE_KEY = "performance_repository_periods_restore"
            private const val ACTUAL_QUARTER_RESTORE_KEY = "performance_repository_quarter_restore"
        }
    }
}

data class SerializableList(
    val list: List<Pair<String, String>>,
) : Serializable