package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.presentation.DayUi
import com.maxim.diaryforstudents.news.presentation.Reload
import java.text.SimpleDateFormat
import java.util.Calendar

interface DiaryRepository {
    suspend fun init(reload: Reload)
    fun data(date: Int): DiaryData.Day
    fun actualDate(): Int
    fun dayList(today: Int): List<DayUi>

    class Base(private val cloudDataSource: DiaryCloudDataSource): DiaryRepository {
        override suspend fun init(reload: Reload) {
            cloudDataSource.init(reload)
        }

        override fun data(date: Int): DiaryData.Day {
            val list = cloudDataSource.data()
            return DiaryData.Day(date, list.filter { it.isDate(date) }.ifEmpty { listOf(DiaryData.Empty) })
        }

        override fun actualDate() = (System.currentTimeMillis() / 86400000).toInt()
        override fun dayList(today: Int): List<DayUi> {
            val formatter = SimpleDateFormat("E")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = today * 86400000L
            val dayOfTheWeek = when(formatter.format(calendar.time)) {
                "Mon" -> 1
                "Tue" -> 2
                "Wed" -> 3
                "Thu" -> 4
                "Fri" -> 5
                "Sat" -> 6
                else -> 7
            }
            return listOf(
                DayUi(today - dayOfTheWeek + 1, dayOfTheWeek == 1),
                DayUi(today - dayOfTheWeek + 2, dayOfTheWeek == 2),
                DayUi(today - dayOfTheWeek + 3, dayOfTheWeek == 3),
                DayUi(today - dayOfTheWeek + 4, dayOfTheWeek == 4),
                DayUi(today - dayOfTheWeek + 5, dayOfTheWeek == 5),
                DayUi(today - dayOfTheWeek + 6, dayOfTheWeek == 6),
                DayUi(today - dayOfTheWeek + 7, dayOfTheWeek == 7),
            )
        }
    }
}