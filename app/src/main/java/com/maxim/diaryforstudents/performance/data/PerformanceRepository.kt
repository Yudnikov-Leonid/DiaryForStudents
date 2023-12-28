package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.news.presentation.Reload

interface PerformanceRepository {
    fun changeQuarter(new: Int)
    fun data(search: String): List<PerformanceData>
    fun actualQuarter(): Int
    fun init(reload: Reload)
    fun changeType(type: String)

    class Base(
        private val dataSource: PerformanceCloudDataSource
    ) : PerformanceRepository {
        private var quarter = 0
        override fun changeQuarter(new: Int) {
            quarter = new
        }

        override fun data(search: String): List<PerformanceData> {
            val data = dataSource.data(quarter)
            return if (data.isEmpty()) listOf(PerformanceData.Empty)
            else if (search.isNotEmpty()) {
                data.filter { it.search(search) }
            } else data
        }

        override fun actualQuarter() = quarter

        override fun init(reload: Reload) {
            val leapYears = ((System.currentTimeMillis() / 86400000) / 365 / 4).toInt()
            var day = (System.currentTimeMillis() / 86400000) % 365 - leapYears
            if (day < 0) day += 365
            quarter = when (day) {
                in 0..91 -> 3
                in 92..242 -> 4
                in 243..305 -> 1
                else -> 2
            }
            dataSource.init(reload)
        }

        override fun changeType(type: String) {
            dataSource.changeType(type)
        }
    }
}