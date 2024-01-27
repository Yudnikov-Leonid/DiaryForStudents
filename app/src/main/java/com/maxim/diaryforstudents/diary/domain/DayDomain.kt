package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.diary.presentation.DayUi

data class DayDomain(
    private val date: Int,
    private val isSelected: Boolean
) {
    fun toUi() = DayUi(date, isSelected)
}