package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.diary.data.room.MenuLessonsDao
import com.maxim.diaryforstudents.diary.data.room.RoomLesson
import com.maxim.diaryforstudents.performance.common.data.HandleMarkType
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import java.util.Calendar

interface DiaryRepository {
    fun dayLists(today: Int): Triple<List<DayData>, List<DayData>, List<DayData>>
    suspend fun day(date: Int): DiaryData.Day
    fun actualDate(): Int
    fun homeworks(date: Int): String
    fun previousHomeworks(date: Int): String

    suspend fun getLesson(lessonName: String, date: String): DiaryData.Lesson
    suspend fun menuLesson(): Pair<List<DiaryData.Lesson>, Int>

    class Base(
        private val service: DiaryService,
        private val menuService: MenuLessonsDao,
        private val mapper: DiaryData.Mapper<RoomLesson>,
        private val formatter: Formatter,
        private val eduUser: EduUser,
        private val manageResource: ManageResource,
        private val handleMarkType: HandleMarkType,
    ) : DiaryRepository {
        private val cache = mutableMapOf<String, DiaryData.Day>()

        override fun dayLists(today: Int): Triple<List<DayData>, List<DayData>, List<DayData>> {
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
            val currentList = listOf(
                DayData(today - dayOfTheWeek + 1, dayOfTheWeek == 1),
                DayData(today - dayOfTheWeek + 2, dayOfTheWeek == 2),
                DayData(today - dayOfTheWeek + 3, dayOfTheWeek == 3),
                DayData(today - dayOfTheWeek + 4, dayOfTheWeek == 4),
                DayData(today - dayOfTheWeek + 5, dayOfTheWeek == 5),
                DayData(today - dayOfTheWeek + 6, dayOfTheWeek == 6),
                DayData(today - dayOfTheWeek + 7, dayOfTheWeek == 7),
            )

            val nextList = listOf(
                DayData(today - dayOfTheWeek + 8, false),
                DayData(today - dayOfTheWeek + 9, false),
                DayData(today - dayOfTheWeek + 10, false),
                DayData(today - dayOfTheWeek + 11, false),
                DayData(today - dayOfTheWeek + 12, false),
                DayData(today - dayOfTheWeek + 13, false),
                DayData(today - dayOfTheWeek + 14, false),
            )

            val previousList = listOf(
                DayData(today - dayOfTheWeek - 6, false),
                DayData(today - dayOfTheWeek - 5, false),
                DayData(today - dayOfTheWeek - 4, false),
                DayData(today - dayOfTheWeek - 3, false),
                DayData(today - dayOfTheWeek - 2, false),
                DayData(today - dayOfTheWeek - 1, false),
                DayData(today - dayOfTheWeek, false),
            )
            return Triple(previousList, currentList, nextList)
        }

        override suspend fun day(date: Int): DiaryData.Day {
            val formattedDate = formatter.format("dd.MM.yyyy", date)
            cache[formattedDate]?.let { return it }

            val data = service
                .getDay(DiaryBody(formattedDate, eduUser.apikey(), eduUser.guid(), ""))
            val day = if (data.success) DiaryData.Day(date,
                data.data.map { lesson ->
                    DiaryData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.LESSON_NUMBER,
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
                                it.GRADE_TYPE_GUID?.let { typeGuid ->
                                    handleMarkType.handle(typeGuid)
                                } ?: MarkType.Current,
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
            val start = manageResource.string(R.string.homework_from)
            val sb = StringBuilder("$start $formattedDate\n\n")
            homeworks.filter { it.second.isNotEmpty() }.forEach {
                sb.append("${it.first}: ${it.second}\n\n")
            }
            return sb.trim().toString()
        }

        override fun previousHomeworks(date: Int): String {
            val formattedDate = formatter.format("dd.MM.yyyy", date)
            val data = cache[formattedDate]!!
            val homeworks = data.previousHomeworks()
            val start = manageResource.string(R.string.homework_for)
            val sb = StringBuilder("$start $formattedDate\n\n")
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
                    formattedDate, eduUser.apikey(), eduUser.guid(), ""
                )
            )
            if (data.success) {
                return data.data.filter { it.SUBJECT_NAME == lessonName }.first().let { lesson ->
                    DiaryData.Lesson(
                        lesson.SUBJECT_NAME,
                        lesson.LESSON_NUMBER,
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
                                it.GRADE_TYPE_GUID?.let { typeGuid ->
                                    handleMarkType.handle(typeGuid)
                                } ?: MarkType.Current,
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

        override suspend fun menuLesson(): Pair<List<DiaryData.Lesson>, Int> {
            val cachedLessons = menuService.lessons()
            val lessons: List<DiaryData>
            if (cachedLessons.isEmpty() || cachedLessons[0].date != actualDate()) {
                val today = formatter.format("dd.MM.yyyy", actualDate())
                val day: DiaryData
                try {
                    day = if (cache[today] == null) day(actualDate()) else cache[today]!!
                } catch (e: Exception) {
                    return Pair(emptyList(), 0)
                }
                if (day.lessons().first() is DiaryData.Empty)
                    return Pair(emptyList(), 0)
                menuService.clear()
                day.lessons().forEach {
                    menuService.insert(it.map(mapper))
                }
                lessons = day.lessons()
            } else {
                lessons = menuService.lessons().map {
                    DiaryData.Lesson(
                        it.name, it.number, it.teacherName, it.topic,
                        it.homework, it.previousHomework, it.startTime, it.endTime,
                        it.date, emptyList(), emptyList(), emptyList()
                    )
                }
            }

            var currentLesson = -1
            var isBreak = false
            val time = formatter.hoursAndMinutes(System.currentTimeMillis()).toInt()
            if (time in lessons[0].period().first..lessons[0].period().second) {
                currentLesson = 0
            } else {
                for (i in 1..lessons.lastIndex) {
                    if (time in lessons[i - 1].period().second..lessons[i].period().second) {
                        currentLesson = i
                        if (time in (lessons[i - 1].period().second..lessons[i].period().first))
                            isBreak = true
                        break
                    }
                }
            }

            val newList = mutableListOf<DiaryData>()
            lessons.forEachIndexed { i, it ->
                newList.add(it.addMenuState(when{
                    i < currentLesson -> MenuLessonState.Passed
                    i == currentLesson && isBreak -> MenuLessonState.Break
                    i == currentLesson -> MenuLessonState.IsGoingOnNow
                    i - 1 == currentLesson -> MenuLessonState.Next
                    else -> MenuLessonState.Default
                }))
            }

            return Pair(
                if (currentLesson != -1) (newList as List<DiaryData.Lesson>) else emptyList(),
                currentLesson,
            )
        }
    }
}