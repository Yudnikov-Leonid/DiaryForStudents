package com.maxim.diaryforstudents.diary.domain

data class DayDomain(
    private val date: Int,
    private val isSelected: Boolean
) {
    interface Mapper<T> {
        fun map(date: Int, isSelected: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(date, isSelected)
}