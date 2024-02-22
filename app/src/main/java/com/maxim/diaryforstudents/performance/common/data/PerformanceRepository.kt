package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.analytics.data.AnalyticsData
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.performance.common.room.MarkRoom
import com.maxim.diaryforstudents.performance.common.room.PerformanceDao
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.Serializable
import java.util.Calendar

interface PerformanceRepository : SaveAndRestore {
    suspend fun loadData()
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
        private val handleResponse: HandleResponse,
        private val dao: PerformanceDao
    ) : PerformanceRepository {
        private var loadException: Exception? = null

        private val responseCache = mutableMapOf<Int, List<CloudLesson>>()
        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()
        private val finalResponseCache = mutableListOf<PerformanceFinalLesson>()

        private val checkedMarksCache: MutableMap<String, MutableList<CloudMark>> = mutableMapOf()

        private val periods = mutableListOf<Pair<String, String>>()
        private var currentQuarter = 1

        override suspend fun loadData() {
            responseCache.clear()
            finalResponseCache.clear()
            finalCache.clear()
            cache.clear()
            loadException = null

            checkedMarksCache.clear()
            dao.checkedMarks().forEach {
                if (checkedMarksCache[it.lessonName] == null)
                    checkedMarksCache[it.lessonName] = ArrayList()
                checkedMarksCache[it.lessonName]!!.add(CloudMark(it.date, it.value, it.markType))
            }

            try {
                if (periods.isEmpty()) {
                    loadPeriods()
                }
                val dates = periods[currentQuarter - 1]
                coroutineScope {
                    listOf(
                        async {
                            responseCache[currentQuarter] =
                                cloudDataSource.data(dates.first, dates.second)
                        },
                        async {
                            finalResponseCache.addAll(cloudDataSource.finalData())
                        }
                    ).awaitAll()
                }

                finalCache.addAll(handleResponse.finalMarksLessons(finalResponseCache))
                cache.addAll(
                    handleResponse.lessons(
                        responseCache[currentQuarter]!!,
                        checkedMarksCache,
                        true,
                        currentQuarter
                    )
                )
                val checkedMarks = mutableListOf<MarkRoom>()
                responseCache[currentQuarter]!!.forEach { lesson ->
                    lesson.MARKS.forEach {
                        checkedMarks.add(
                            MarkRoom(
                                "${lesson.SUBJECT_NAME}-${it.DATE}",
                                it.VALUE,
                                it.DATE,
                                lesson.SUBJECT_NAME,
                                it.GRADE_TYPE_GUID
                            )
                        )
                    }
                }
                dao.clearAll()
                checkedMarks.forEach {
                    dao.insert(it)
                }
            } catch (e: Exception) {
                loadException = e
            }
        }

        private suspend fun loadPeriods() {
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
                currentQuarter = i + 1
                break
            }
            periods.add(Pair(periods.first().first, periods.last().second))
        }

        override fun cachedData() =
            loadException?.let {
                throw it
            } ?: cache.ifEmpty { listOf(PerformanceData.Empty) }

        override fun cachedFinalData() =
            loadException?.let { throw it }
                ?: finalCache.ifEmpty { listOf(PerformanceData.Empty) }

        override suspend fun changeQuarter(quarter: Int) {
            loadException = null
            cache.clear()
            try {
                val dates = periods[quarter - 1]
                if (responseCache[quarter] == null) {
                    responseCache[quarter] = cloudDataSource.data(dates.first, dates.second)
                }
                cache.addAll(
                    handleResponse.lessons(
                        responseCache[quarter]!!,
                        checkedMarksCache,
                        quarter == currentQuarter(),
                        quarter
                    )
                )
            } catch (e: Exception) {
                loadException = e
            }
        }

        override suspend fun analytics(
            quarter: Int,
            lessonName: String,
            interval: Int,
            showFinal: Boolean
        ): List<AnalyticsData> {
            val dates = periods[quarter - 1] //!!
            if (responseCache[quarter] == null) {
                responseCache[quarter] = cloudDataSource.data(dates.first, dates.second)
            }
            val list = ArrayList(
                handleResponse.analytics(
                    responseCache[quarter]!!,
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

        override fun currentQuarter() = currentQuarter

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(
                RESTORE_KEY,
                PerformanceRepositorySave(
                    periods,
                    responseCache,
                    cache,
                    finalCache,
                    finalResponseCache
                )
            )
            bundleWrapper.save(ACTUAL_QUARTER_RESTORE_KEY, currentQuarter)
            handleResponse.save(bundleWrapper)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            val data = bundleWrapper.restore<PerformanceRepositorySave>(RESTORE_KEY)
            periods.addAll(
                data?.periods ?: emptyList()
            )
            currentQuarter = bundleWrapper.restore(ACTUAL_QUARTER_RESTORE_KEY) ?: 1
            data?.responseCache?.forEach {
                if (responseCache.isEmpty())
                    responseCache[it.key] = it.value
            }
            data?.cache?.let {
                if (cache.isEmpty())
                    cache.addAll(it)
            }
            data?.finalCache?.let {
                if (finalCache.isEmpty())
                    finalCache.addAll(it)
            }
            data?.finalResponseCache?.let {
                if (finalResponseCache.isEmpty())
                    finalResponseCache.addAll(it)
            }
            handleResponse.restore(bundleWrapper)
        }

        companion object {
            private const val RESTORE_KEY = "performance_repository_restore"
            private const val ACTUAL_QUARTER_RESTORE_KEY = "performance_repository_quarter_restore"
        }
    }
}

data class PerformanceRepositorySave(
    val periods: List<Pair<String, String>>,
    val responseCache: Map<Int, List<CloudLesson>>,
    val cache: List<PerformanceData>,
    val finalCache: List<PerformanceData>,
    val finalResponseCache: List<PerformanceFinalLesson>
) : Serializable