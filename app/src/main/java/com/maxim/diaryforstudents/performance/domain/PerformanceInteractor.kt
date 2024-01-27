package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.eduData.PerformanceData

//todo need domain model
interface PerformanceInteractor {
    suspend fun init()
    fun data(search: String): List<PerformanceData>
    fun finalData(search: String): List<PerformanceData>
    suspend fun changeQuarter(quarter: Int)

    fun actualQuarter(): Int
}