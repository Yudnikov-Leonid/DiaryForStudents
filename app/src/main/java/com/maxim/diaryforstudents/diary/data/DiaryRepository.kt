package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import java.util.Calendar

interface DiaryRepository {
    fun dayList(today: Int): List<DayData>
    suspend fun day(date: Int): DiaryData.Day
    fun actualDate(): Int
    fun homeworks(date: Int): String
    fun previousHomeworks(date: Int): String

    suspend fun getLesson(lessonName: String, date: String): DiaryData.Lesson

    fun saveFilters(booleanArray: BooleanArray)
    fun filters(): BooleanArray
    fun saveHomeworkFrom(value: Boolean)
    fun homeworkFrom(): Boolean

    class Base(
        private val service: DiaryService,
        private val formatter: Formatter,
        private val eduUser: EduUser,
        private val simpleStorage: SimpleStorage
    ) : DiaryRepository {
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

            val data = service
                .getDay(DiaryBody(formattedDate, BuildConfig.SHORT_API_KEY, eduUser.guid(), ""))
            val day = if (data.success) DiaryData.Day(date,
                data.data.map { lesson ->
                    DiaryData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.TEACHER_NAME,
                        lesson.TOPIC ?: "",
                        lesson.HOMEWORK ?: "",
                        lesson.HOMEWORK_PREVIOUS?.HOMEWORK ?: "",
                        lesson.LESSON_TIME_BEGIN,
                        lesson.LESSON_TIME_END,
                        date,
                        lesson.MARKS?.map {
                            PerformanceData.Mark(
                                it.VALUE,
                                MarkType.Current,
                                formattedDate,
                                lesson.SUBJECT_NAME,
                                false,
                                true
                            )
                        }
                            ?: emptyList(),
                        lesson.ABSENCE.map { it.SHORT_NAME },
                        lesson.NOTES
                    )
                }.ifEmpty { listOf(DiaryData.Empty) }
            ) else throw ServiceUnavailableException(data.message)
            cache[formattedDate] = day
            return day
        }

        override fun actualDate() = (System.currentTimeMillis() / 86400000).toInt()

        override fun homeworks(date: Int): String {
            val formattedDate = formatter.format("dd.MM.yyyy", date)
            val data = cache[formattedDate]!!
            val homeworks = data.homeworks()
            val sb = StringBuilder("Homework from $formattedDate\n\n")
            homeworks.filter { it.second.isNotEmpty() }.forEach {
                sb.append("${it.first}: ${it.second}\n\n")
            }
            return sb.trim().toString()
        }

        override fun previousHomeworks(date: Int): String {
            val formattedDate = formatter.format("dd.MM.yyyy", date)
            val data = cache[formattedDate]!!
            val homeworks = data.previousHomeworks()
            val sb = StringBuilder("Homework for $formattedDate\n\n")
            homeworks.filter { it.second.isNotEmpty() }.forEach {
                sb.append("${it.first}: ${it.second}\n\n")
            }
            return sb.trim().toString()
        }

        override suspend fun getLesson(lessonName: String, date: String): DiaryData.Lesson {
            val calendar = Calendar.getInstance().apply {
                val splitDate = date.split(".")
                set(Calendar.DAY_OF_MONTH, splitDate[0].toInt())
                set(Calendar.MONTH, splitDate[1].toInt() - 1)
                set(Calendar.YEAR, splitDate[2].toInt())
            }
            val formattedDate =
                formatter.format("dd.MM.yyyy", (calendar.timeInMillis / 86400000).toInt())
            val data = service.getDay(
                DiaryBody(
                    formattedDate, BuildConfig.SHORT_API_KEY, eduUser.guid(), ""
                )
            )
            if (data.success) {
                return data.data.filter { it.SUBJECT_NAME == lessonName }.first().let { lesson ->
                    DiaryData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.TEACHER_NAME,
                        lesson.TOPIC ?: "",
                        lesson.HOMEWORK ?: "",
                        lesson.HOMEWORK_PREVIOUS?.HOMEWORK ?: "",
                        lesson.LESSON_TIME_BEGIN,
                        lesson.LESSON_TIME_END,
                        (calendar.timeInMillis / 86400000).toInt(),
                        lesson.MARKS?.map {
                            PerformanceData.Mark(
                                it.VALUE,
                                MarkType.Current,
                                formattedDate,
                                lesson.SUBJECT_NAME,
                                false,
                                true
                            )
                        }
                            ?: emptyList(),
                        lesson.ABSENCE.map { it.SHORT_NAME },
                        lesson.NOTES
                    )
                }
            } else throw ServiceUnavailableException(data.message)
        }

        override fun saveFilters(booleanArray: BooleanArray) {
            simpleStorage.save(HOMEWORK_FILTER, booleanArray[0])
            simpleStorage.save(TOPIC_FILTER, booleanArray[1])
            simpleStorage.save(MARKS_FILTER, booleanArray[2])
        }

        override fun filters() = booleanArrayOf(
            simpleStorage.read(HOMEWORK_FILTER, false),
            simpleStorage.read(TOPIC_FILTER, false),
            simpleStorage.read(MARKS_FILTER, false),
        )

        override fun saveHomeworkFrom(value: Boolean) {
            simpleStorage.save(HOMEWORK_FROM, value)
        }

        override fun homeworkFrom() = simpleStorage.read(HOMEWORK_FROM, true)

        companion object {
            private const val HOMEWORK_FILTER = "homework_filter"
            private const val TOPIC_FILTER = "topic_filter"
            private const val MARKS_FILTER = "marks_filter"
            private const val HOMEWORK_FROM = "homework_from"
        }
    }
}