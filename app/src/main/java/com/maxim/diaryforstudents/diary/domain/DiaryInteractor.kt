package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.diary.data.DayData
import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.performance.common.data.FailureHandler

interface DiaryInteractor {
    fun dayLists(today: Int): Triple<List<DayDomain>, List<DayDomain>, List<DayDomain>>
    suspend fun day(date: Int): DiaryDomain
    fun actualDate(): Int
    fun homeworks(date: Int): String
    fun previousHomeworks(date: Int): String

    suspend fun initMenuLessons()
    fun menuLessons(): List<DiaryDomain.Lesson>
    fun currentLesson(): Int
    fun isBreak(): Boolean

    class Base(
        private val repository: DiaryRepository,
        private val failureHandler: FailureHandler,
        private val mapper: DiaryData.Mapper<DiaryDomain>,
        private val dayMapper: DayData.Mapper<DayDomain>,
        private val manageResource: ManageResource
    ) : DiaryInteractor {
        override fun dayLists(today: Int): Triple<List<DayDomain>, List<DayDomain>, List<DayDomain>> {
            val data = repository.dayLists(today)
            return Triple(
                data.first.map { it.map(dayMapper) },
                data.second.map { it.map(dayMapper) },
                data.third.map { it.map(dayMapper) })
        }

        override suspend fun day(date: Int): DiaryDomain {
            return try {
                repository.day(date).map(mapper)
            } catch (e: Exception) {
                DiaryDomain.Error(failureHandler.handle(e).message(manageResource))
            }
        }

        override fun actualDate() = repository.actualDate()
        override fun homeworks(date: Int) = repository.homeworks(date)
        override fun previousHomeworks(date: Int) = repository.previousHomeworks(date)

        private var menuLessons = Triple(emptyList<DiaryDomain.Lesson>(), 0, false)
        override suspend fun initMenuLessons() {
            val result = repository.menuLesson()
            menuLessons = Triple(result.first.map { it.map(mapper) as DiaryDomain.Lesson }, result.second, result.third)
        }
        override fun menuLessons() = menuLessons.first
        override fun currentLesson() = menuLessons.second
        override fun isBreak() = menuLessons.third
    }
}