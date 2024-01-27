package com.maxim.diaryforstudents.diary.domain

interface DiaryInteractor {
    fun dayList(today: Int): List<DayDomain>
    suspend fun day(date: Int): DiaryDomain.Day
    fun cachedDay(date: Int): DiaryDomain.Day
    fun actualDate(): Int
    fun homeworks(date: Int): String
    fun previousHomeworks(date: Int): String

    fun saveFilters(booleanArray: BooleanArray)
    fun filters(): BooleanArray
    fun saveHomeworkFrom(value: Boolean)
    fun homeworkFrom(): Boolean
}