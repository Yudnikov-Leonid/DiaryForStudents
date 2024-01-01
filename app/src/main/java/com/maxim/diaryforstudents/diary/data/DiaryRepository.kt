package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.core.presentation.Reload
import java.util.Calendar

interface DiaryRepository {
    suspend fun init(reload: Reload, week: Int)
    fun date(date: Int): DiaryData.Day
    fun actualDate(): Int
    fun dayList(today: Int): List<DayData>

    class Base(private val cloudDataSource: DiaryCloudDataSource) : DiaryRepository {
        override suspend fun init(reload: Reload, week: Int) {
            cloudDataSource.init(reload, week)
        }

        override fun date(date: Int): DiaryData.Day {
            val list = cloudDataSource.data()
            return DiaryData.Day(
                date,
                list.filter { it.isDate(date) }.ifEmpty { listOf(DiaryData.Empty) })
        }

        override fun actualDate() = (System.currentTimeMillis() / 86400000).toInt()
        override fun dayList(today: Int): List<DayData> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = today * 86400000L
            val dayOfTheWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> 1
                Calendar.TUESDAY -> 2
                Calendar.WEDNESDAY -> 3
                Calendar.THURSDAY -> 4
                Calendar.FRIDAY -> 5
                Calendar.SATURDAY -> 6
                Calendar.SUNDAY -> 7
                else -> 0
            }
            return listOf(
                DayData(today - dayOfTheWeek + 1, dayOfTheWeek == 1),
                DayData(today - dayOfTheWeek + 2, dayOfTheWeek == 2),
                DayData(today - dayOfTheWeek + 3, dayOfTheWeek == 3),
                DayData(today - dayOfTheWeek + 4, dayOfTheWeek == 4),
                DayData(today - dayOfTheWeek + 5, dayOfTheWeek == 5),
                DayData(today - dayOfTheWeek + 6, dayOfTheWeek == 6),
                DayData(today - dayOfTheWeek + 7, dayOfTheWeek == 7),
            )
        }
    }
}