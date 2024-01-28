package com.maxim.diaryforstudents.diary.data

data class DayData(
    private val date: Int,
    private val isSelected: Boolean
) {
    interface Mapper<T> {
        fun map(date: Int, isSelected: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(date, isSelected)
}