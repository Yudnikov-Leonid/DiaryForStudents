package com.maxim.diaryforstudents.diary.eduData

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.diary.data.DayData
import com.maxim.diaryforstudents.diary.data.DiaryData
import java.util.Calendar

interface EduDiaryRepository {
    fun dayList(today: Int): List<DayData>
    suspend fun day(date: Int): DiaryData.Day
    fun actualDate(): Int

    class Base(
        private val service: EduDiaryService,
        private val formatter: Formatter,
        private val eduUser: EduUser
    ) : EduDiaryRepository {
        private val cache = mutableMapOf<String, DiaryData.Day>()

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

        override suspend fun day(date: Int): DiaryData.Day {
            val formattedDate = formatter.format("dd.MM.yyyy", date)
            cache[formattedDate]?.let { return it }

            val data =
                service.getDay(EduDiaryBody(formattedDate, BuildConfig.SHORT_API_KEY, eduUser.guid(), ""))
            val day = if (data.success) DiaryData.Day(
                date,
                data.data.map { lesson ->
                    DiaryData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.TOPIC ?: "",
                        lesson.HOMEWORK ?: "",
                        lesson.LESSON_TIME_BEGIN,
                        lesson.LESSON_TIME_END,
                        date
                    )
                }.ifEmpty { listOf(DiaryData.Empty) }
            ) else DiaryData.Day(0, emptyList())
            cache[formattedDate] = day
            return day
        }

        override fun actualDate() = (System.currentTimeMillis() / 86400000).toInt()
    }
}