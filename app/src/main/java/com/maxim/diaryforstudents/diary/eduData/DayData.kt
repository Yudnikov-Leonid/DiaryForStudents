package com.maxim.diaryforstudents.diary.eduData

import com.maxim.diaryforstudents.diary.domain.DayDomain

data class DayData(
    private val date: Int,
    private val isSelected: Boolean
) {
    fun toDomain() = DayDomain(date, isSelected)
}