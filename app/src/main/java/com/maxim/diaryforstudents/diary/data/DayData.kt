package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.presentation.DayUi

data class DayData(
    private val date: Int,
    private val isSelected: Boolean
) {
    fun toUi() = DayUi(date, isSelected)
}