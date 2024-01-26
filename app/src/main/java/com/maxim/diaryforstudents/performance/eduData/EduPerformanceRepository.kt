package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.performance.data.PerformanceData
import java.util.Calendar

interface EduPerformanceRepository {
    suspend fun init()
    fun cachedData(): List<PerformanceData>
    fun cachedData(search: String): List<PerformanceData>
    fun cachedFinalData(): List<PerformanceData>
    fun cachedFinalData(search: String): List<PerformanceData>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int

    class Base(private val cloudDataSource: EduPerformanceCloudDataSource) :
        EduPerformanceRepository {
        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()

        override suspend fun init() {
            cache.clear()
            try {
                cache.addAll(cloudDataSource.data(actualQuarter()))
            } catch (e: Exception) {
                cache.add(PerformanceData.Error(e.message!!))
            }


            finalCache.clear()
            try {
                finalCache.addAll(cloudDataSource.finalData())
            } catch (e: Exception) {
                finalCache.add(PerformanceData.Error(e.message!!))
            }
        }

        override fun cachedData() = cache.ifEmpty { listOf(PerformanceData.Empty) }
        override fun cachedData(search: String) =
            cache.filter { it.search(search) }.ifEmpty { listOf(PerformanceData.Empty) }

        override fun cachedFinalData() = finalCache.ifEmpty { listOf(PerformanceData.Empty) }
        override fun cachedFinalData(search: String) =
            finalCache.filter { it.search(search) }.ifEmpty { listOf(PerformanceData.Empty) }

        override suspend fun changeQuarter(quarter: Int) {
            cache.clear()
            try {
                cache.addAll(cloudDataSource.data(quarter))
            } catch (e: Exception) {
                cache.add(PerformanceData.Error(e.message!!))
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