package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.analytics.data.AnalyticsData
import java.io.Serializable
import java.util.Calendar

interface PerformanceRepository : Communication.Save, Communication.Restore {
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

        private val periods = mutableListOf<Pair<String, String>>()
        private var actualQuarter = 1

        override suspend fun initActual() {
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
                val dates = periods[actualQuarter() - 1]
                responseCache[actualQuarter()] = cloudDataSource.data(dates.first, dates.second)
                cache.addAll(
                    handleResponse.lessons(responseCache[actualQuarter()]!!, true, actualQuarter())
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
                finalCache.addAll(handleResponse.finalMarksLessons(finalResponseCache))
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
                val dates = periods[quarter - 1]
                cache.addAll(
                    handleResponse.lessons(
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
                        actualQuarter()
                    )
                )
            return list
        }

        override fun actualQuarter() = actualQuarter

        override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(PERIODS_RESTORE_KEY, SerializableList(periods))
            bundleWrapper.save(ACTUAL_QUARTER_RESTORE_KEY, actualQuarter)
            handleResponse.save(bundleWrapper)
        }

        override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
            //todo надо ли так много сейвить?
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