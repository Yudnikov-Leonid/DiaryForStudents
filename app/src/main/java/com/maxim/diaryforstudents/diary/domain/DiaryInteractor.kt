package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.core.sl.ManageResource
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

    class Base(
        private val repository: DiaryRepository,
        private val failureHandler: FailureHandler,
        private val manageResource: ManageResource
    ) : DiaryInteractor {
        override fun dayLists(today: Int): Triple<List<DayDomain>, List<DayDomain>, List<DayDomain>> {
            val data = repository.dayLists(today)
            return Triple(
                data.first,
                data.second,
                data.third
            )
        }

        override suspend fun day(date: Int): DiaryDomain {
            return try {
                repository.day(date)
            } catch (e: Exception) {
                DiaryDomain.Error(failureHandler.handle(e).message(manageResource))
            }
        }

        override fun actualDate() = repository.actualDate(true)
        override fun homeworks(date: Int) = repository.homeworks(date)
        override fun previousHomeworks(date: Int) = repository.previousHomeworks(date)

        private var menuLessons = Pair(emptyList<DiaryDomain.Lesson>(), 0)
        override suspend fun initMenuLessons() {
            val result = repository.menuLesson()
            menuLessons = Pair(result.first, result.second)
        }

        override fun menuLessons() = menuLessons.first
        override fun currentLesson() = menuLessons.second
    }
}