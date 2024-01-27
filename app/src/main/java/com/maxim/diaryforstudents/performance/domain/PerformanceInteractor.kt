package com.maxim.diaryforstudents.performance.domain

interface PerformanceInteractor {
    suspend fun init()
    fun data(search: String): List<PerformanceDomain>
    fun finalData(search: String): List<PerformanceDomain>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int
}