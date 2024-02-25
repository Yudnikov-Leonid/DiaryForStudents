package com.maxim.diaryforstudents.diary.domain

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

    fun saveFilters(booleanArray: BooleanArray)
    fun filters(): BooleanArray
    fun saveHomeworkFrom(value: Boolean)
    fun homeworkFrom(): Boolean

    class Base(
        private val repository: DiaryRepository,
        private val failureHandler: FailureHandler,
        private val mapper: DiaryData.Mapper<DiaryDomain>,
        private val dayMapper: DayData.Mapper<DayDomain>
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
                DiaryDomain.Error(failureHandler.handle(e).message())
            }
        }

        override fun actualDate() = repository.actualDate()
        override fun homeworks(date: Int) = repository.homeworks(date)
        override fun previousHomeworks(date: Int) = repository.previousHomeworks(date)
        override fun saveFilters(booleanArray: BooleanArray) = repository.saveFilters(booleanArray)
        override fun filters() = repository.filters()
        override fun saveHomeworkFrom(value: Boolean) = repository.saveHomeworkFrom(value)
        override fun homeworkFrom() = repository.homeworkFrom()
    }
}