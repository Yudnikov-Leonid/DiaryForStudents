package com.maxim.diaryforstudents.performance

import android.util.Log
import com.maxim.diaryforstudents.news.Reload

interface PerformanceRepository {
    fun changeQuarter(new: Int)
    fun data(): List<PerformanceData>
    fun actualQuarter(): Int
    fun init(reload: Reload)

    class Base(
        private val dataSource: PerformanceCloudDataSource
    ) : PerformanceRepository {
        private var quarter = 0
        override fun changeQuarter(new: Int) {
            quarter = new
        }

        override fun data(): List<PerformanceData> {
            val data = dataSource.data(quarter)
            return data.ifEmpty { listOf(PerformanceData.Empty) }
        }

        override fun actualQuarter() = quarter

        override fun init(reload: Reload) {
            val leapYears = ((System.currentTimeMillis() / 86400000) / 365 / 4).toInt()
            var day = (System.currentTimeMillis() / 86400000) % 365 - leapYears
            if (day < 0) day += 365
            Log.d("MyLog", "$day")
            quarter = when (day) {
                in 0..91 -> 3
                in 92..242 -> 4
                in 243..305 -> 1
                else -> 2
            }
            dataSource.init(reload)
        }
    }
}