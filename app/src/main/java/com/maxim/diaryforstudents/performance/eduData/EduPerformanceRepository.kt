package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.performance.data.PerformanceData

interface EduPerformanceRepository {
    suspend fun init()
    fun cachedData(): List<PerformanceData>
    fun cachedData(search: String): List<PerformanceData>
    fun cachedFinalData(): List<PerformanceData>
    fun cachedFinalData(search: String): List<PerformanceData>

    class Base(private val cloudDataSource: EduLoginCloudDataSource) :
        EduPerformanceRepository {
        private val cache = mutableListOf<PerformanceData>()
        private val finalCache = mutableListOf<PerformanceData>()

        override suspend fun init() {
            cache.clear()
            cache.addAll(cloudDataSource.data())

            finalCache.clear()
            finalCache.addAll(cloudDataSource.finalData())
        }

        override fun cachedData() = cache
        override fun cachedData(search: String) = cache.filter { it.search(search) }

        override fun cachedFinalData() = finalCache
        override fun cachedFinalData(search: String) = finalCache.filter { it.search(search) }
    }
}